package com.example.schedule.API

import com.example.schedule.POJO.Lesson
import com.example.schedule.POJO.Object_
import com.example.schedule.POJO.Schedule
import com.example.schedule.POJO.Subobject
import com.example.schedule.ScheduleConstants
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.util.*
import kotlin.jvm.Throws

class ScheduleTypeAdapter : TypeAdapter<Schedule?>() {
    override fun write(out: JsonWriter, value: Schedule?) {
        throw UnsupportedOperationException()
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Schedule? {
        val lessons: MutableList<Lesson> = ArrayList()
        var oList: MutableList<Object_?>
        var from: String?
        var to: String?
        var type: String?
        `in`.beginArray()
        while (`in`.peek() == JsonToken.BEGIN_OBJECT) {
            `in`.beginObject()
            val o = Object_()
            `in`.nextName()
            from = `in`.nextString()
            `in`.nextName()
            to = `in`.nextString()
            `in`.nextName()
            type = `in`.nextString()
            `in`.nextName()
            if (`in`.peek() == JsonToken.BEGIN_ARRAY) {
                `in`.beginArray()
                while (`in`.hasNext()) {
                    `in`.beginObject()
                    `in`.nextName()
                    o.subtype = `in`.nextString()
                    parser(`in`, o)
                    `in`.endObject()
                }
                if (`in`.peek() == JsonToken.END_OBJECT) {
                    `in`.endObject()
                } else if (`in`.peek() == JsonToken.END_ARRAY) {
                    `in`.endArray()
                }
            } else if (`in`.peek() == JsonToken.BEGIN_OBJECT) {
                `in`.beginObject()
                `in`.nextName()
                o.subtype = `in`.nextString()
                parser(`in`, o)
                `in`.endObject()
            }
            oList = ArrayList()
            oList.add(o)
            `in`.endObject()
            lessons.add(Lesson(from, to, type, oList))
        }
        `in`.endArray()
        return Schedule(lessons)
    }

    companion object {
        @Throws(IOException::class)
        private fun parser(`in`: JsonReader, o: Object_) {
            if (`in`.hasNext()) { //Имеется ли объект после subtype?
                `in`.nextName()
                if (`in`.peek() == JsonToken.BEGIN_ARRAY) {
                    `in`.beginArray()
                    val list: MutableList<Subobject> = ArrayList()
                    while (`in`.hasNext()) {
                        `in`.beginObject()
                        val s = Subobject()
                        while (`in`.hasNext()) when (`in`.nextName()) {
                            ScheduleConstants.LABEL -> `in`.nextString()
                            ScheduleConstants.SUBJECT -> s.subject = `in`.nextString()
                            ScheduleConstants.TEACHER -> s.teacher = `in`.nextString()
                            ScheduleConstants.AUDITORY -> s.auditory = `in`.nextString()
                        }
                        `in`.endObject()
                        list.add(s)
                    }
                    o.subobject = list
                    `in`.endArray()
                } else if (`in`.peek() == JsonToken.BEGIN_OBJECT) {
                    `in`.beginObject()
                    val s = Subobject()
                    while (`in`.hasNext()) when (`in`.nextName()) {
                        ScheduleConstants.SUBJECT -> s.subject = `in`.nextString()
                        ScheduleConstants.TEACHER -> s.teacher = `in`.nextString()
                        ScheduleConstants.AUDITORY -> s.auditory = `in`.nextString()
                    }
                    val list: MutableList<Subobject> = ArrayList()
                    list.add(s)
                    o.subobject = list
                    `in`.endObject()
                }
            }
        }
    }
}
