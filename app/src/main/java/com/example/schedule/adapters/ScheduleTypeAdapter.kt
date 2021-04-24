package com.example.schedule.adapters

import com.example.schedule.POJO.Lesson
import com.example.schedule.POJO.Object_
import com.example.schedule.POJO.Schedule
import com.example.schedule.POJO.Subobject
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
        val lessons = ArrayList<Lesson>()
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
            input.endObject()
            lessons.add(Lesson(from, to, type, arrayListOf(o)))
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
                    val list = ArrayList<Subobject>()
                    while (input.hasNext()) {
                        input.beginObject()
                        val s = Subobject()
                        while (input.hasNext()) when (input.nextName()) {
                            LABEL -> input.nextString()
                            SUBJECT -> s.subject = input.nextString()
                            TEACHER -> s.teacher = input.nextString()
                            PREFIX -> s.subjectPrefix = input.nextString()
                            AUDITORY -> s.auditory = input.nextString()
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
                        SUBJECT -> s.subject = input.nextString()
                        TEACHER -> s.teacher = input.nextString()
                        AUDITORY -> s.auditory = input.nextString()
                    }
                    o.subobject = arrayListOf(s)
                    input.endObject()
                }
            }
        }
       private const val TEACHER = "teacher"
       private const val AUDITORY = "class"
       private const val SUBJECT = "subject"
       private const val PREFIX = "prefix"
       private const val LABEL = "label"
    }

}
