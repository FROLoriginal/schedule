package com.example.schedule.API;

import com.example.schedule.POJO.Object_;
import com.example.schedule.POJO.Schedule;
import com.example.schedule.POJO.Subobject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleTypeAdapter extends TypeAdapter<Schedule> {

    @Override
    public void write(JsonWriter out, Schedule value) {
         throw new UnsupportedOperationException();
    }

    @Override
    public Schedule read(JsonReader in) throws IOException {
        in.beginArray();
        in.beginObject();

        Object_ o = new Object_();
        in.nextName();
        final String from = in.nextString();
        in.nextName();
        final String to = in.nextString();
        in.nextName();
        final String type = in.nextString();
        in.nextName();
        if (in.peek() == JsonToken.BEGIN_ARRAY) {
            in.beginArray();
            in.nextName();
            while (in.hasNext()) {
                in.beginObject();
                in.nextName();
                o.setSubtype(in.nextString());
                parser(in, o);
                in.endObject();
                in.nextName();
            }

            in.endArray();
        } else if (in.peek() == JsonToken.BEGIN_OBJECT) {
            in.beginObject();

            in.nextName();
            o.setSubtype(in.nextString());
            parser(in, o);
            in.endObject();
        }
        in.endArray();
        return new Schedule(from, to, type, o);
    }

    private static void parser(JsonReader in, Object_ o) throws IOException {
        if (in.hasNext()) { //Имеется ли объект после subtype?
            in.nextName();
            if (in.peek() == JsonToken.BEGIN_ARRAY) {
                in.beginArray();

                List<Subobject> list = new ArrayList<>();
                in.nextName();
                while (in.hasNext()) {

                    in.beginObject();
                    Subobject s = new Subobject();

                    while (in.hasNext())
                        switch (in.nextName()) {
                            case "subject":
                                s.setSubject(in.nextString());
                                break;
                            case "teacher":
                                s.setTeacher(in.nextString());
                                break;
                            case "class":
                                s.setAuditory(in.nextString());
                                break;
                        }
                    in.endObject();
                    list.add(s);
                    in.nextName();
                }
                o.setSubobject(list);
                in.endArray();
            } else if (in.peek() == JsonToken.BEGIN_OBJECT) {

                in.beginObject();

                Subobject s = new Subobject();
                while (in.hasNext())
                    switch (in.nextName()) {
                        case "subject":
                            s.setSubject(in.nextString());
                            break;
                        case "teacher":
                            s.setTeacher(in.nextString());
                            break;
                        case "class":
                            s.setAuditory(in.nextString());
                            break;
                    }
                List<Subobject> list = new ArrayList<>();
                list.add(s);
                o.setSubobject(list);
                in.endObject();

            }
        }
    }

}
