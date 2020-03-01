package com.example.schedule.API;

import com.example.schedule.POJO.Schedule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "https://artfpr.ru";
    private Retrofit mRetrofit;

    private NetworkService() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Schedule.class, new ScheduleTypeAdapter())
                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }
    public ApiHolder getJSONApi() {
        return mRetrofit.create(ApiHolder.class);
    }
}
