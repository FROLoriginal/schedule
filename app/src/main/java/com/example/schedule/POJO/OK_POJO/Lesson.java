package com.example.schedule.POJO.OK_POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Lesson {
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("object")
    @Expose
    private List<Object_> object;

    public Lesson(String from, String to, String type, List<Object_> object) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.object = object;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Object_> getObject() {
        return object;
    }

    public void setObject(List<Object_> object) {
        this.object = object;
    }
}

