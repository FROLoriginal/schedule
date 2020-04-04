package com.example.schedule.POJO.OK_POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subobject {

  @SerializedName("subject")
  @Expose
  private String subject = "null";

  @SerializedName("teacher")
  @Expose
  private String teacher = "null";

  @SerializedName("class")
  @Expose
  private String auditory = "null";


  public String getSubject() {
    return subject;
  }

  public void setSubject(String object) {
    this.subject = object;
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
}
