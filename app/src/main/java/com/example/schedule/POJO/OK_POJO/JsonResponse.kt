package com.example.schedule.POJO.OK_POJO

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JsonResponse {
    @SerializedName("schedule")
    @Expose
    var schedule: List<Schedule>? = null

}
