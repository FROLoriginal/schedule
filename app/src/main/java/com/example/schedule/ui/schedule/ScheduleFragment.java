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

import com.example.schedule.R;
import com.example.schedule.SQL.SQLManager;
import com.example.schedule.Utils;
import com.example.schedule.ui.MainActivity;

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

        SharedPreferences pref = getActivity().getSharedPreferences(SQLManager.SHARED_PREFERENCES_TABLES, Context.MODE_PRIVATE);
        String table = pref.getString(SQLManager.SHARED_PREFERENCES_TABLE, null);

        SQLManager sqlManager = new SQLManager(getActivity().getBaseContext(), table, null, 1);
        SQLiteDatabase db = sqlManager.getReadableDatabase();

        List<SimplifiedScheduleModel> data = fillSchedule(db, table);
        recyclerView.addItemDecoration(getDecorator(recyclerView, data));
        recyclerView.setAdapter(new ScheduleRecyclerViewAdapter(new ArrayList<>(data)));

        MainActivity activity = (MainActivity) getActivity();
        activity.getSupportActionBar().hide();

        return root;
    }

    private ScheduleHeaderItemDecorator getDecorator(RecyclerView recyclerView, final List<SimplifiedScheduleModel> data) {
        return new ScheduleHeaderItemDecorator
                (recyclerView, new ScheduleHeaderItemDecorator.StickyHeaderInterface() {

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
                        return R.layout.schedule_header_card_view;
                    }

                    @Override
                    public void bindHeaderData(View header, int headerPosition) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_WEEK, Utils.Time.convertDayOfWeekToUS(data.get(headerPosition).getDayOfWeek()));
                        String displayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                        ((TextView) header.findViewById(R.id.day_of_week_header)).setText(Utils.toUpperCaseFirstLetter(displayName));
                    }
                    @Override
                    public boolean isHeader(int itemPosition) {
                        return data.get(itemPosition).isHeader();
                    }
                });
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
               // SQLManager.BOTH_NUMERATOR_DIVIDER  // index 8
        };
        List<SimplifiedScheduleModel> week = new ArrayList<>();

        for (int dayOfWeek = 1; dayOfWeek < 7; dayOfWeek++) {

            Cursor c = db.query(tableName,
                    columns,
                    SQLManager.DAY_OF_WEEK + " = ?",
                    new String[]{String.valueOf(dayOfWeek)},
                    null, null, null);

            SimplifiedScheduleModel s = new SimplifiedScheduleModel();
            s.setDayOfWeek(dayOfWeek);
            s.setHeader(true);
            week.add(s);
//***********************************************************************
/*            int weekStatus = getWeekStatus();

            if (num) weekStatus = SQLManager.NUMERATOR;
            else weekStatus = SQLManager.DIVIDER;

            String selection = SQLManager.DAY_OF_WEEK + " = ? AND (" +
                    SQLManager.BOTH_NUMERATOR_DIVIDER + " = ? OR " +
                    SQLManager.BOTH_NUMERATOR_DIVIDER + " = ? )";
            String[] selectionArgs = new String[]{
                    String.valueOf(dayOfWeek),
                    String.valueOf(SQLManager.BOTH),
                    String.valueOf(weekStatus)};

            Cursor c = db.query(tableName,
                    columns,
                    selection,
                  selectionArgs,
                    null, null, null);

            SimplifiedScheduleModel s = new SimplifiedScheduleModel();
            s.setDayOfWeek(dayOfWeek);
            s.setHeader(true);
            week.add(s);

*/
//***********************************************************************
            while (c.moveToNext()) {

                SimplifiedScheduleModel lesson = new SimplifiedScheduleModel();

                lesson.setFrom(c.getString(3));
                lesson.setTo(c.getString(4));
                lesson.setAuditory(c.getString(5));
                lesson.setSubject(c.getString(1));
                lesson.setTeacher(c.getString(2));
                lesson.setCounter(c.getInt(6));
                lesson.setTypeOfSubject(c.getString(7));
                lesson.setDayOfWeek(dayOfWeek);

                week.add(lesson);

            }
            c.close();
        }
        return week;

    }
}
