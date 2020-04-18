package com.example.schedule.ui.schedule;

public class SimplifiedScheduleModel {

    private String from;
    private String to;
    private String teacher;
    private String auditory;
    private String typeOfSubject;
    private String subject;
    private int counter;


    public SimplifiedScheduleModel() {
    }

    public SimplifiedScheduleModel(SimplifiedScheduleModel model) {

        from = model.getFrom();
        to = model.getTo();
        teacher = model.getTeacher();
        auditory = model.getAuditory();
        typeOfSubject = model.getTypeOfSubject();
        subject = model.getSubject();
        counter = model.getCounter();

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

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getAuditory() {
        return auditory;
    }

    public void setAuditory(String auditory) {
        this.auditory = auditory;
    }

    public String getTypeOfSubject() {
        return typeOfSubject;
    }

    public void setTypeOfSubject(String typeOfSubject) {
        this.typeOfSubject = typeOfSubject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
