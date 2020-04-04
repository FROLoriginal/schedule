package com.example.schedule.API;

import com.example.schedule.POJO.OK_POJO.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiHolder {

    String API_GET_SCHEDULE_METHOD = "/rudn/schedule/api/teams.get";
    String VERSION = "v";
    String ACCESS_TOKEN = "accessToken";
    String TEAM = "team";

    int REQUIRED_FIELDS_ARE_MISSING = 2;
    int INCORRECT_ACCESS_TOKEN = 4;
    int UNKNOWN_GROUP = 100;

    @GET(API_GET_SCHEDULE_METHOD)
    Call<Response> getSchedule(@Query(VERSION) int v,
                                      @Query(ACCESS_TOKEN) String accessToken,
                                      @Query(TEAM) String team);
}
