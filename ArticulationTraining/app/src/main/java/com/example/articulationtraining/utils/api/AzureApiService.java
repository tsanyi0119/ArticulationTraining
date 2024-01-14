package com.example.articulationtraining.utils.api;

import com.example.articulationtraining.utils.api.response.SpeechRecognitionResponse;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AzureApiService {
    @POST("speech/recognition/conversation/cognitiveservices/v1?language=zh-TW")
    Observable<SpeechRecognitionResponse> recognizeSpeech(
            @Header("Authorization") String authorization,
            @Header("Content-Type") String contentType,
            @Header("Ocp-Apim-Subscription-Key") String subscriptionKey,
            @Body RequestBody audioFile
    );
}
