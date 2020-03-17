package com.example.schedule.API;

import com.example.schedule.POJO.OK_POJO.Object_;
import com.example.schedule.POJO.OK_POJO.Schedule;
import com.example.schedule.POJO.OK_POJO.Subobject;
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

        List<String> from = new ArrayList<>();
        List<String> to = new ArrayList<>();
        List<String> type = new ArrayList<>();
        List<Object_> oList = new ArrayList<>();

        in.beginArray();
        while (in.peek() == JsonToken.BEGIN_OBJECT) {
            in.beginObject();
            Object_ o = new Object_();
            in.nextName();
            from.add(in.nextString());
            in.nextName();
            to.add(in.nextString());
            in.nextName();
            type.add(in.nextString());
            in.nextName();

            if (in.peek() == JsonToken.BEGIN_ARRAY) {
                in.beginArray();
                while (in.hasNext()) {
                    in.beginObject();
                    in.nextName();
                    o.setSubtype(in.nextString());
                    parser(in, o);
                    in.endObject();
                }
                if (in.peek() == JsonToken.END_OBJECT) {
                    in.endObject();
                } else if (in.peek() == JsonToken.END_ARRAY) {
                    in.endArray();
                }
            } else if (in.peek() == JsonToken.BEGIN_OBJECT) {
                in.beginObject();

                in.nextName();
                o.setSubtype(in.nextString());
                parser(in, o);
                in.endObject();
            }
            oList.add(o);
            in.endObject();
        }
        in.endArray();
        return new Schedule(from, to, type, oList);
    }

    private static void parser(JsonReader in, Object_ o) throws IOException {
        if (in.hasNext()) { //Имеется ли объект после subtype?
            in.nextName();
            if (in.peek() == JsonToken.BEGIN_ARRAY) {
                in.beginArray();

                List<Subobject> list = new ArrayList<>();
                while (in.hasNext()) {

                    in.beginObject();
                    Subobject s = new Subobject();

                    while (in.hasNext())
                        switch (in.nextName()) {
                            case "label":
                                in.nextString();
                                break;
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
