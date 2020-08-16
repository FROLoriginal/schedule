package com.example.schedule.POJO

import com.example.schedule.POJO.Error
import com.example.schedule.POJO.JsonResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Response {
    @SerializedName("response")
    @Expose
    var jsonResponse: JsonResponse? = null

    @SerializedName("error")
    @Expose
    var error: Error? = null

}
