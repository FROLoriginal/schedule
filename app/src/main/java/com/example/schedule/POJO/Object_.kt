package com.example.schedule.POJO

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Object_ {
    @SerializedName("subtype")
    @Expose
    lateinit var subtype : String

    @SerializedName("subobject")
    @Expose
    var subobject: List<Subobject>? = null

    @SerializedName("name")
    @Expose
    lateinit var name : String

}
