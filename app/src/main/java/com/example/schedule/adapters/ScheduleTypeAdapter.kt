package com.example.schedule.adapters

import com.example.schedule.POJO.Lesson
import com.example.schedule.POJO.Object_
import com.example.schedule.POJO.Schedule
import com.example.schedule.POJO.Subobject
import com.example.schedule.Util.ScheduleConstants
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
    override fun read(input: JsonReader): Schedule? {
        val lessons: MutableList<Lesson> = ArrayList()
        var oList: MutableList<Object_?>
        var from: String?
        var to: String?
        var type: String?
        input.beginArray()
        while (input.peek() == JsonToken.BEGIN_OBJECT) {
            input.beginObject()
            val o = Object_()
            input.nextName()
            from = input.nextString()
            input.nextName()
            to = input.nextString()
            input.nextName()
            type = input.nextString()
            input.nextName()
            if (input.peek() == JsonToken.BEGIN_ARRAY) {
                input.beginArray()
                while (input.hasNext()) {
                    input.beginObject()
                    input.nextName()
                    o.subtype = input.nextString()
                    parser(input, o)
                    input.endObject()
                }
                if (input.peek() == JsonToken.END_OBJECT) {
                    input.endObject()
                } else if (input.peek() == JsonToken.END_ARRAY) {
                    input.endArray()
                }
            } else if (input.peek() == JsonToken.BEGIN_OBJECT) {
                input.beginObject()
                input.nextName()
                o.subtype = input.nextString()
                parser(input, o)
                input.endObject()
            }
            oList = ArrayList()
            oList.add(o)
            input.endObject()
            lessons.add(Lesson(from, to, type, oList))
        }
        input.endArray()
        return Schedule(lessons)
    }

    companion object {
        @Throws(IOException::class)
        private fun parser(input: JsonReader, o: Object_) {
            if (input.hasNext()) { //Имеется ли объект после subtype?
                input.nextName()
                if (input.peek() == JsonToken.BEGIN_ARRAY) {
                    input.beginArray()
                    val list: MutableList<Subobject> = ArrayList()
                    while (input.hasNext()) {
                        input.beginObject()
                        val s = Subobject()
                        while (input.hasNext()) when (input.nextName()) {
                            ScheduleConstants.LABEL -> input.nextString()
                            ScheduleConstants.SUBJECT -> s.subject = input.nextString()
                            ScheduleConstants.TEACHER -> s.teacher = input.nextString()
                            ScheduleConstants.AUDITORY -> s.auditory = input.nextString()
                        }
                        input.endObject()
                        list.add(s)
                    }
                    o.subobject = list
                    input.endArray()
                } else if (input.peek() == JsonToken.BEGIN_OBJECT) {
                    input.beginObject()
                    val s = Subobject()
                    while (input.hasNext()) when (input.nextName()) {
                        ScheduleConstants.SUBJECT -> s.subject = input.nextString()
                        ScheduleConstants.TEACHER -> s.teacher = input.nextString()
                        ScheduleConstants.AUDITORY -> s.auditory = input.nextString()
                    }
                    val list: MutableList<Subobject> = ArrayList()
                    list.add(s)
                    o.subobject = list
                    input.endObject()
                }
            }
        }
    }
}
