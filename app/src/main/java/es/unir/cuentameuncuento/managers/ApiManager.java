package es.unir.cuentameuncuento.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.unir.cuentameuncuento.api.ApiService;
import es.unir.cuentameuncuento.api.models.ImageRequestBody;
import es.unir.cuentameuncuento.api.models.ImageResponseBody;
import es.unir.cuentameuncuento.api.models.Message;
import es.unir.cuentameuncuento.api.models.SpeechRequestBody;
import es.unir.cuentameuncuento.api.models.StoryRequestBody;
import es.unir.cuentameuncuento.api.models.StoryResponseBody;
import es.unir.cuentameuncuento.api.models.Choice;
import es.unir.cuentameuncuento.helpers.ApiKeyReader;
import es.unir.cuentameuncuento.models.Book;
import es.unir.cuentameuncuento.utils.AudioFileConverter;
import es.unir.cuentameuncuento.utils.ImageConverter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static final String BASE_URL = "https://api.openai.com/v1/";
    private ApiService apiService;
    private Call<ImageResponseBody> currentImageCall;
    private Call<StoryResponseBody> currentStoryCall;
    private Call<ResponseBody> currentSpeechCall;
    private Context context;

    public ApiManager(Context context) {

        this.context = context;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor apiKeyInterceptor = chain -> {
            Request originalRequest = chain.request();
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", ApiKeyReader.getApiKeyOpenAi(context))
                    .header("Content-Type", "application/json")
                    .build();
            return chain.proceed(newRequest);
        };


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);
        httpClient.addInterceptor(apiKeyInterceptor);
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }

    public void generateStory(String category, String character, StoryCallback storyCallback) {

        List<Message> messages = new ArrayList<Message>();
        StoryRequestBody requestBody = new StoryRequestBody();
        messages.add(new Message("system","Eres un generador de cuentos infantiles que generaras cuentos personalizados segun categoria y personaje. Ten en cuenta de no utilizar lenguaje soez y se creativo."));
        messages.add(new Message("user","Generame un cuento de la siguiente categoria y personaje"+ category + character));

        requestBody.setMessages(messages);
        requestBody.setModel("gpt-3.5-turbo");

        storyCallback.onStartCreation();

        currentStoryCall = apiService.generateStory(requestBody);
        currentStoryCall.enqueue(new Callback<StoryResponseBody>() {
            @Override
            public void onResponse(Call<StoryResponseBody> call, Response<StoryResponseBody> response) {
                if (response.isSuccessful()) {

                    StoryResponseBody storyResponseBody = response.body();
                    List<Choice> choices = storyResponseBody.getChoices();
                    Message message = choices.get(0).getMessage();

                        Book newBook = new Book();
                        newBook.setTitle("Cuento de " + category + " de " + character);
                        newBook.setNarrative(message.getContent());
                        storyCallback.onStoryGenerated(newBook);

                } else {
                    int errorCode = response.code();
                    String errorMessage = response.message();
                    storyCallback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<StoryResponseBody> call, Throwable t) {
                    storyCallback.onError("Story: " + t.getMessage());
            }
        });
    }

    public interface StoryCallback {
        void onStartCreation();
        void onStoryGenerated(Book story);
        void onError(String mensajeError);
    }

    public void generateSpeech(Book story, SpeechCallback speechCallback) {

            SpeechRequestBody requestBody = new SpeechRequestBody();

            requestBody.setModel("tts-1");
            requestBody.setInput(story.getNarrative());
            requestBody.setVoice("nova");

             speechCallback.onStartCreation();

            currentSpeechCall = apiService.generateSpeech(requestBody);
            currentSpeechCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            byte[] speechBytes = response.body().bytes();
                            File audioFile = AudioFileConverter.convertBytesToAudioFile(speechBytes);
                            speechCallback.onSpeechGenerated(audioFile);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                        String errorMessage = response.message();
                        speechCallback.onError(errorMessage);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                speechCallback.onError(t.getMessage());
                }
            });


    }

    public interface SpeechCallback {
        void onStartCreation();
        void onSpeechGenerated( File audioFile) throws IOException;
        void onError(String mensajeError);
    }
    public void generateImage(String category, String character,ImageCallback imageCallback) {
        ImageRequestBody requestBody = new ImageRequestBody();
        requestBody.setModel("dall-e-3");
        requestBody.setPrompt("Ilustra un solo cuerpo de un/a protagonista llamado/a ["+ character +"] en estilo de dibujos animados, asociado a la categoría [" + category +"]." +
                "La ilustración es para una historia corta para niños de entre 2 y 8 años." +
                "Debe tener un fondo completamente blanco y no debe incluir texto, rótulos, armas ni otros elementos adicionales." +
                "Solo debe aparecer un personaje en la imagen." );
        requestBody.setN(1);
        requestBody.setSize("1024x1024");

        currentImageCall = apiService.generateImage(requestBody);
        currentImageCall.enqueue(new Callback<ImageResponseBody>() {
            @Override
            public void onResponse(Call<ImageResponseBody> call, Response<ImageResponseBody> response) {
                if (response.isSuccessful()) {

                    ImageResponseBody responseBody = response.body();

                    String url = responseBody.getData().get(0).getUrl();
                    ImageConverter imageConverter = new ImageConverter();

                    imageConverter.convertUrlToBitmap(url, new ImageConverter.BitmapCallback() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap) {
                            imageCallback.onImageGenerated(bitmap);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            imageCallback.onError("ImageConverter: " + errorMessage);
                        }
                    });


                }else{
                    String errorMessage = response.message();
                    imageCallback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ImageResponseBody> call, Throwable t) {
                imageCallback.onError("Image:" + t.getMessage());
            }
        });
    }
    public interface ImageCallback {
        void onStartCreation();
        void onImageGenerated(Bitmap imageBitmap) ;
        void onError(String mensajeError);
    }

    public void cancelCalls() {
        if (currentImageCall != null && !currentImageCall.isCanceled()) {
            currentImageCall.cancel();
            Log.d("DESTROY:", "SE DESTRUYE imagecall" );
        }
        if (currentSpeechCall != null && !currentSpeechCall.isCanceled()) {
            currentSpeechCall.cancel();
            Log.d("DESTROY:", "SE DESTRUYE speechcall" );
        }
        if (currentStoryCall != null && !currentStoryCall.isCanceled()) {
            currentStoryCall.cancel();
            Log.d("DESTROY:", "SE DESTRUYE storycall" );
        }
    }

}