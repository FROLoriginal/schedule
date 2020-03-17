package com.example.schedule.POJO.OK_POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonResponse {

    @SerializedName("schedule")
    @Expose
    private List<Schedule> dayOfWeek;

    public List<Schedule> getSchedule() {
        return dayOfWeek;
    }

    public void setSchedule(List<Schedule> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

}
