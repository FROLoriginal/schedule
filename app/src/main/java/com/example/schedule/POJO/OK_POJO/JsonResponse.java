package com.example.schedule.POJO.OK_POJO;

import com.example.schedule.POJO.ERROR_POJO.Error;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonResponse {

    @SerializedName("error")
    @Expose
    private Error error;

    @SerializedName("schedule")
    @Expose
    private List<Schedule> dayOfWeek;

    /*@SerializedName("labels")
    @Expose
    private List<Labels> labels;
    */
    public List<Schedule> getSchedule() {
        return dayOfWeek;
    }

    public void setSchedule(List<Schedule> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /*
    public List<Labels> getLabels() {
        return labels;
    }

    public void setLabels(List<Labels> labels) {
        this.labels = labels;
    }
    */

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
