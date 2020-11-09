package com.example.schedule.API

import com.example.schedule.POJO.Schedule
import com.example.schedule.adapters.ScheduleTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService private constructor() {
    companion object {
        private var mInstance: NetworkService? = null
        private const val BASE_URL = "https://api.schedule.artfpr.ru"
        private lateinit var mRetrofit: Retrofit

        fun getInstance() = mInstance ?: NetworkService()
    }

    init {
        val gson: Gson = GsonBuilder()
                .registerTypeAdapter(Schedule::class.java, ScheduleTypeAdapter())
                .create()

        mRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    fun getJSONApi(): ApiHolder = mRetrofit.create(ApiHolder::class.java)
}
