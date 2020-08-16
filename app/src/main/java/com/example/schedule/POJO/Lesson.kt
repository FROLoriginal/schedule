package com.example.schedule.POJO

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Lesson(
        @field:Expose
        @field:SerializedName("from")
        var from: String,
        @field:Expose
        @field:SerializedName("to")
        var to: String,
        @field:Expose
        @field:SerializedName("type")
        var type: String,
        @field:Expose
        @field:SerializedName("object")
        var `object`: MutableList<Object_?>)
