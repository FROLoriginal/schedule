package com.example.schedule.ui.schedule;

import com.example.schedule.Utils;

import java.util.List;

class SimplifiedScheduleModel {

    private String from;
    private String to;
    private String teacher;
    private String auditory;
    private String typeOfSubject;
    private String subject;
    private String styleOfSubject;
    private int dayOfWeek;
    private int counter;
    private boolean isHeader = false;
    private List<SimplifiedScheduleModel> ifOptionally;
    private static boolean isNumerator;

    SimplifiedScheduleModel() {
    }

    public SimplifiedScheduleModel(SimplifiedScheduleModel model) {

        this.from = model.getFrom();
        this.to = model.getTo();
        this.teacher = model.getTeacher();
        this.auditory = model.getAuditory();
        this.typeOfSubject = model.getTypeOfSubject();
        this.subject = model.getSubject();
        this.styleOfSubject = model.getStyleOfSubject();
        this.dayOfWeek = model.getDayOfWeek();
        this.counter = model.getCounter();
        this.isHeader = model.isHeader();
        this.ifOptionally = model.getIfOptionally();
    }

    public List<SimplifiedScheduleModel> getIfOptionally() {
        return ifOptionally;
    }

    public void setIfOptionally(final List<SimplifiedScheduleModel> ifOptionally) {
        this.ifOptionally = ifOptionally;
    }

    public void setStyleOfSubject(String styleOfSubject) {
        this.styleOfSubject = styleOfSubject;
    }

    public static boolean isNumerator() {
        return isNumerator;
    }

    public static void setIsNumerator(boolean isNumerator) {
        SimplifiedScheduleModel.isNumerator = isNumerator;
    }

    public String getStyleOfSubject() {
        return styleOfSubject;
    }

    public void setStleOfSubject(String styleOfSubject) {
        this.styleOfSubject = styleOfSubject;
    }

    String getFrom() {
        return from;
    }

    void setFrom(String from) {
        this.from = from;
    }

    String getTo() {
        return to;
    }

    void setTo(String to) {
        this.to = to;
    }

    int getDayOfWeek() {
        return dayOfWeek;
    }

    void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    boolean isHeader() {
        return isHeader;
    }

    void setHeader(boolean header) {
        isHeader = header;
    }

    String getTeacher() {
        return teacher;
    }

    void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    String getAuditory() {
        return auditory;
    }

    void setAuditory(String auditory) {
        this.auditory = auditory;
    }

    String getTypeOfSubject() {
        return typeOfSubject;
    }

    void setTypeOfSubject(String typeOfSubject) {
        this.typeOfSubject = typeOfSubject;
    }

    String getSubject() {
        return subject;
    }

    void setSubject(String subject) {
        this.subject = subject;
    }

    int getCounter() {
        return counter;
    }

    void setCounter(int counter) {
        this.counter = counter;
    }

    String getFormattedTime() {

        return from + " - " + to;
    }

    String getAuditoryWithStyleOfSubject() {

        switch (Utils.typeOfSubject(subject)) {

            case Utils.SEMINAR:
                return "Семинар, " + auditory;
            case Utils.LABORATORY_WORK:
                return "Лаб. " + auditory;
            case Utils.LECTURE:
                return "Лекция, " + auditory;
            case Utils.OTHER:
                return auditory;

        }
        return auditory;
    }
}
