package com.example.schedule.API;

import com.example.schedule.POJO.JsonResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiHolder {

    @GET("/rudn/lib/schedule-example.json")
    public Call<JsonResponse> getPostWithID();
}
