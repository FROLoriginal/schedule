package com.example.schedule.POJO.OK_POJO;

import java.util.List;

public class Schedule {

    private List<Lesson> lessons;

    public Schedule(List<Lesson> lessons){

        this.lessons = lessons;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
