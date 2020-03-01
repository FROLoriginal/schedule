package com.example.schedule.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Schedule {
    @SerializedName("from")
    @Expose
    private List<String> from;
    @SerializedName("to")
    @Expose
    private List<String> to;
    @SerializedName("type")
    @Expose
    private List<String> type;
    @SerializedName("object")
    @Expose
    private List<Object_> object;

    public Schedule(List<String> from, List<String> to, List<String> type, List<Object_> object) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.object = object;
    }

    public List<String> getFrom() {
        return from;
    }

    public void setFrom(List<String> from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<Object_> getObject() {
        return object;
    }

    public void setObject(List<Object_> object) {
        this.object = object;
    }
}
