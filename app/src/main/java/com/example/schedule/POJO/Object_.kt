package com.example.schedule.POJO

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Object_ {
    @SerializedName("subtype")
    @Expose
    var subtype = "null"

    @SerializedName("subobject")
    @Expose
    var subobject: List<Subobject>? = null

    @SerializedName("name")
    @Expose
    var name = "null"

}
