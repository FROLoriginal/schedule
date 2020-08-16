package com.example.schedule.POJO

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Error {
    @SerializedName("errorCode")
    @Expose
    var errorCode = 0

    @SerializedName("errorMsg")
    @Expose
    var errorMsg: String? = null

}
