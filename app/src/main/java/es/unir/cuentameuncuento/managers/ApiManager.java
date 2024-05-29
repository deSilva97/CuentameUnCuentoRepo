package es.unir.cuentameuncuento.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import es.unir.cuentameuncuento.R;
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
    private final ApiService apiService;
    private Call<ImageResponseBody> currentImageCall;
    private Call<StoryResponseBody> currentStoryCall;
    private Call<ResponseBody> currentSpeechCall;

    Context context;

    public ApiManager(Context context) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        this.context = context;

        Interceptor apiKeyInterceptor = chain -> {
            Request originalRequest = chain.request();
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", Objects.requireNonNull(ApiKeyReader.getApiKeyOpenAi(context)))
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

    public void generateStory(String category, String character, int duration ,StoryCallback storyCallback) {

        List<Message> messages = new ArrayList<>();
        StoryRequestBody requestBody = new StoryRequestBody();

        String rol = String.format(context.getString(R.string.prompt_rol));
        String message = String.format(context.getString(R.string.prompt_message), category, duration, character);

        messages.add(new Message("system",rol));
        messages.add(new Message("user", message));

        requestBody.setMessages(messages);
        requestBody.setModel("gpt-3.5-turbo");

        storyCallback.onStartCreation();

        currentStoryCall = apiService.generateStory(requestBody);
        currentStoryCall.enqueue(new Callback<StoryResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponseBody> call, @NonNull Response<StoryResponseBody> response) {
                if (response.isSuccessful()) {

                    StoryResponseBody storyResponseBody = response.body();
                    assert storyResponseBody != null;
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
            public void onFailure(@NonNull Call<StoryResponseBody> call, @NonNull Throwable t) {
                    storyCallback.onError("Story: " + t.getMessage());
            }
        });
    }

    public interface StoryCallback {
        void onStartCreation();
        void onStoryGenerated(Book story);
        void onError(String messageError);
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
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            assert response.body() != null;
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
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                speechCallback.onError(t.getMessage());
                }
            });


    }

    public interface SpeechCallback {
        void onStartCreation();
        void onSpeechGenerated( File audioFile) throws IOException;
        void onError(String messageError);
    }
    public void generateImage(String category, String character,ImageCallback imageCallback) {
        ImageRequestBody requestBody = new ImageRequestBody();
        requestBody.setModel("dall-e-3");
        requestBody.setPrompt(String.format(context.getString(R.string.prompt_image), character, category));
        requestBody.setN(1);
        requestBody.setSize("1024x1024");
        requestBody.setQuality("hd");

        currentImageCall = apiService.generateImage(requestBody);
        currentImageCall.enqueue(new Callback<ImageResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ImageResponseBody> call, @NonNull Response<ImageResponseBody> response) {
                if (response.isSuccessful()) {

                    ImageResponseBody responseBody = response.body();

                    assert responseBody != null;
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
            public void onFailure(@NonNull Call<ImageResponseBody> call, @NonNull Throwable t) {
                imageCallback.onError("Image:" + t.getMessage());
            }
        });
    }
    public interface ImageCallback {
        void onStartCreation();
        void onImageGenerated(Bitmap imageBitmap) ;
        void onError(String messageError);
    }

    public void cancelCalls() {
        if (currentImageCall != null && !currentImageCall.isCanceled()) {
            currentImageCall.cancel();
            Log.d("DESTROY:", "DESTROY image call" );
        }
        if (currentSpeechCall != null && !currentSpeechCall.isCanceled()) {
            currentSpeechCall.cancel();
            Log.d("DESTROY:", "DESTROY speech-call" );
        }
        if (currentStoryCall != null && !currentStoryCall.isCanceled()) {
            currentStoryCall.cancel();
            Log.d("DESTROY:", "DESTROY story call" );
        }
    }

}