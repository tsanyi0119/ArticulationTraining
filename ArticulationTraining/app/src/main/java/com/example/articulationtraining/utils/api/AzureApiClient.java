package com.example.articulationtraining.utils.api;

import androidx.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AzureApiClient {
    public static final String Whisper_URL = "https://api.openai.com";
    public static final String Azure_URL = "https://eastasia.stt.speech.microsoft.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Azure_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
    @NonNull
    public AzureApiService getAzureApiService() {
        return getClient().create(AzureApiService.class);
    }
}
