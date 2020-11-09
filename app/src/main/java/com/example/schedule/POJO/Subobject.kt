package com.example.schedule.POJO

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Subobject {

    @SerializedName("subject")
    @Expose
    var subject = ""

    @SerializedName("teacher")
    @Expose
    var teacher = ""

    @SerializedName("class")
    @Expose
    var auditory = ""

    @SerializedName("prefix")
    @Expose
    var subjectPrefix = ""

}
