package com.example.schedule.POJO.OK_POJO

import com.example.schedule.POJO.ERROR_POJO.Error
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
