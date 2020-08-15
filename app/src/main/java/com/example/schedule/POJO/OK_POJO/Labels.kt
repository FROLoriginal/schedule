package com.example.schedule.POJO.OK_POJO

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Labels {
    @SerializedName("label")
    @Expose
    var label: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

}
