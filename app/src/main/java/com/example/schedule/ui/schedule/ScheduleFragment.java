package com.example.schedule.ui.schedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schedule.POJO.OK_POJO.Lesson;
import com.example.schedule.R;
import com.example.schedule.SQL.SQLManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduleFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_schedule, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Lesson> lessons = new ArrayList<>();

        lessons.add(new Lesson("10", "20", null, null));

        ScheduleHeaderItemDecorator scheduleHeaderItemDecorator = new ScheduleHeaderItemDecorator(recyclerView, new ScheduleHeaderItemDecorator.StickyHeaderInterface() {
            @Override
            public int getHeaderPositionForItem(int itemPosition) {


                for (int headerPosition; itemPosition >= 0; itemPosition--) {
                    if (isHeader(itemPosition)) {
                        headerPosition = itemPosition;
                        return headerPosition;
                    }
                }
                return 0;
            }

            @Override
            public int getHeaderLayout(int headerPosition) {
                //  return R.layout.card_view;
                return R.layout.schedule_header_card_view;
            }

            @Override
            public void bindHeaderData(View header, int headerPosition) {
                if (true) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_WEEK, headerPosition);
                    ((TextView) header.findViewById(R.id.day_of_week_header)).setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
                }
            }

            @Override
            public boolean isHeader(int itemPosition) {
                // System.out.println((itemPosition % 5 == 0) + " : " + itemPosition);
                return itemPosition % 3 == 0;
            }
        });

        SharedPreferences pref = getActivity().getSharedPreferences(SQLManager.SHARED_PREFERENCES_TABLES, Context.MODE_PRIVATE);
        String table = pref.getString(SQLManager.SHARED_PREFERENCES_TABLE, null);

        SQLManager sqlManager = new SQLManager(getActivity().getBaseContext(), table, null, 1);
        SQLiteDatabase db = sqlManager.getReadableDatabase();

        recyclerView.addItemDecoration(scheduleHeaderItemDecorator);
        recyclerView.setAdapter(new ScheduleRecyclerViewAdapter(fillSchedule(db, table)));


        return root;
    }

    private List<SimplifiedScheduleModel> fillSchedule(SQLiteDatabase db, String tableName) {

        String[] columns = new String[]{
                SQLManager.ID,      //index 0
                SQLManager.SUBJECT, //index 1
                SQLManager.TEACHER, //index 2
                SQLManager.FROM,    //index 3
                SQLManager.TO,      //index 4
                SQLManager.AUDITORY,//index 5
                SQLManager.COUNTER, //index 6
                SQLManager.TYPE_OF_SUBJECT // index 7
        };
        List<SimplifiedScheduleModel> week = new ArrayList<>();

        for (int dayOfWeek = 0; dayOfWeek < 6; dayOfWeek++) {

            Cursor c = db.query(tableName,
                    columns,
                    SQLManager.DAY_OF_WEEK + " = ?",
                    new String[]{String.valueOf(dayOfWeek)},
                    null, null, null);

            while (c.moveToNext()) {

                SimplifiedScheduleModel lesson = new SimplifiedScheduleModel();

                lesson.setFrom(c.getString(3));
                lesson.setTo(c.getString(4));
                lesson.setAuditory(c.getString(5));
                lesson.setSubject(c.getString(1));
                lesson.setTeacher(c.getString(2));
                lesson.setCounter(c.getInt(6));
                lesson.setTypeOfSubject(c.getString(7));

                week.add(new SimplifiedScheduleModel(lesson));

            }
            c.close();
        }
        return week;

    }
}
