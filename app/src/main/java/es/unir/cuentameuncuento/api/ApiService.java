package es.unir.cuentameuncuento.api;

import es.unir.cuentameuncuento.api.models.ImageRequestBody;
import es.unir.cuentameuncuento.api.models.ImageResponseBody;
import es.unir.cuentameuncuento.api.models.SpeechRequestBody;
import es.unir.cuentameuncuento.api.models.StoryRequestBody;
import es.unir.cuentameuncuento.api.models.StoryResponseBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("chat/completions")
    Call<StoryResponseBody> generateStory(@Body StoryRequestBody requestBody);

    @POST("audio/speech")
    Call<ResponseBody> generateSpeech(@Body SpeechRequestBody requestBody);

    @POST("images/generations")
    Call<ImageResponseBody> generateImage(@Body ImageRequestBody requestBody);
}
