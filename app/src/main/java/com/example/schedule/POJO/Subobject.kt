package com.example.schedule.POJO

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Subobject {

    @SerializedName("subject")
    @Expose
    var subject = "неизвестно"

    @SerializedName("teacher")
    @Expose
    var teacher = "неизвестно"

    @SerializedName("class")
    @Expose
    var auditory = "неизвестно"
    var counter = 0

}
