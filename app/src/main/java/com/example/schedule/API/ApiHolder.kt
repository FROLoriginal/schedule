package com.example.schedule.API

import com.example.schedule.POJO.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiHolder {
    @GET(API_GET_SCHEDULE_METHOD)
    fun getSchedule(@Query(VERSION) v: Int,
                    @Query(ACCESS_TOKEN) accessToken: String?,
                    @Query(TEAM) team: String?): Call<Response?>?

    companion object {
        const val API_GET_SCHEDULE_METHOD = "teams.get"
        const val VERSION = "v"
        const val ACCESS_TOKEN = "accessToken"
        const val TEAM = "team"
        const val REQUIRED_FIELDS_ARE_MISSING = 2
        const val INCORRECT_ACCESS_TOKEN = 4
        const val UNKNOWN_GROUP = 100
    }
}
