package com.example.schedule.POJO.OK_POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Object_ {

    @SerializedName("subtype")
    @Expose
    private String subtype = "null";

    @SerializedName("subobject")
    @Expose
    private List<Subobject> subobject;

    @SerializedName("name")
    @Expose
    private String name = "null";

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public List<Subobject> getSubobject() {
        return subobject;
    }

    public void setSubobject(List<Subobject> subobject) {
        this.subobject = subobject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
