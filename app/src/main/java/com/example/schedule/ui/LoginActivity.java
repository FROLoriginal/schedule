package com.example.schedule.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedule.API.ApiHolder;
import com.example.schedule.API.NetworkService;
import com.example.schedule.POJO.OK_POJO.JsonResponse;
import com.example.schedule.POJO.OK_POJO.Lesson;
import com.example.schedule.POJO.OK_POJO.Object_;
import com.example.schedule.POJO.OK_POJO.Schedule;
import com.example.schedule.POJO.OK_POJO.Subobject;
import com.example.schedule.R;
import com.example.schedule.SQL.SQLManager;
import com.example.schedule.ScheduleConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText text;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        text = findViewById(R.id.group);
        button = findViewById(R.id.getScheduleButton);
        button.setEnabled(false);
        text.addTextChangedListener(textChangedListener);
        text.setOnEditorActionListener(inputGroup);
    }

    private static final int VERSION = 1;
    private static final String ACCESS_TOKEN = "23a07a867ead26ba489f0b8dc7ab1c330f44a93dc72255e6bed322d5c5577fc2f0517665b03fcbfa794f795833";
    private Call<com.example.schedule.POJO.OK_POJO.Response> responseCall;

    public void onClick(View v) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
        LoginDialogFragment fragment = new LoginDialogFragment(this);
        fragment.show(getSupportFragmentManager(), "dialog_login");
        String editText = text.getText().toString();

        responseCall = NetworkService
                .getInstance()
                .getJSONApi()
                .getSchedule(VERSION, ACCESS_TOKEN, editText);

        new Thread(() -> {
            responseCall
                    .enqueue(new Callback<com.example.schedule.POJO.OK_POJO.Response>() {
                        @Override
                        public void onResponse(@NonNull Call<com.example.schedule.POJO.OK_POJO.Response> call,
                                               @NonNull Response<com.example.schedule.POJO.OK_POJO.Response> response) {
                            com.example.schedule.POJO.OK_POJO.Response r = response.body();
                            JsonResponse jr = r.getJsonResponse();
                            if (r.getError() != null) {
                                switch (r.getError().getErrorCode()) {

                                    case ApiHolder.UNKNOWN_GROUP:
                                        Toast.makeText(getApplicationContext(), R.string.unknownGroup, Toast.LENGTH_LONG).show();
                                        text.setText("");

                                        break;
                                    default:
                                        Toast.makeText(getApplicationContext(), R.string.internalErrorOccurred, Toast.LENGTH_LONG).show();
                                }
                            } else if (jr != null) {
                                String tableName = editText.replace("-", "");
                                SQLManager sqlManager = new SQLManager(getApplicationContext(), tableName, null, 1);
                                SQLiteDatabase sqLiteDatabase = sqlManager.getWritableDatabase();

                                List<ContentValues> cvList = fillDataBase(jr);
                                for (ContentValues cv : cvList) {
                                    sqLiteDatabase.insert(tableName, null, cv);
                                }
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                fragment.dismiss();
                                finish();
                            }
                            fragment.dismiss();
                        }

                        @Override
                        public void onFailure(@NonNull Call<com.example.schedule.POJO.OK_POJO.Response> call,
                                              @NonNull Throwable t) {

                            if (!call.isCanceled()) {
                                Toast.makeText(getApplicationContext(), R.string.connectionError, Toast.LENGTH_LONG).show();
                            }
                            fragment.dismiss();
                        }
                    });
        }).start();
    }

    protected void cancelRequest() {
        if (responseCall != null) responseCall.cancel();
    }

    private List<ContentValues> fillDataBase(JsonResponse jr) {

        List<ContentValues> cvList = new ArrayList<>();

        int daysOfWeek = jr.getSchedule().size();

        for (int dayOfWeek = 0; dayOfWeek < daysOfWeek; dayOfWeek++) {
            ContentValues cv = new ContentValues();
            Schedule schedule = jr.getSchedule().get(dayOfWeek);
            cv.put(SQLManager.DAY_OF_WEEK, dayOfWeek);

            int lessons = schedule.getLessons().size();

            for (int lesson = 0; lesson < lessons; lesson++) {
                cv.put(SQLManager.COUNTER, lesson);
                Lesson les = schedule.getLessons().get(lesson);
                int objects = les.getObject().size();
                cv.put(SQLManager.FROM, les.getFrom());
                cv.put(SQLManager.TO, les.getTo());

                for (int object = 0; object < objects; object++) {
                    Object_ object_ = les.getObject().get(object);
                    String type = les.getType();
                    cv.put(SQLManager.TYPE_OF_SUBJECT, type);
                    if (object_.getSubobject() != null) {
                        int subObjects = object_.getSubobject().size();

                        for (int subObject = 0; subObject < subObjects; subObject++) {
                            Subobject so = object_.getSubobject().get(subObject);
                            cv.put(SQLManager.SUBJECT, so.getSubject());
                            cv.put(SQLManager.AUDITORY, so.getAuditory());
                            cv.put(SQLManager.TEACHER, so.getTeacher());
                            cvList.add(new ContentValues(cv));
                        }
                    } else {
                        if (ScheduleConstants.LessonType.ACTIVITY.equals(type)) {
                            //I don't know why subtype is name of activity subject...
                            cv.put(SQLManager.SUBJECT, object_.getSubtype());
                            cv.put(SQLManager.AUDITORY, "null");
                            cv.put(SQLManager.TEACHER, "null");
                        } else cv.put(SQLManager.SUBJECT, "null");//TODO
                        cvList.add(new ContentValues(cv));
                    }
                }
            }
        }
        return cvList;
    }

    private TextWatcher textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().equals("")) {
                button.setEnabled(false);
            } else {

                button.setEnabled(true);
            }
        }
    };

    private TextView.OnEditorActionListener inputGroup = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_DONE && button.isEnabled()) {

            onClick(null);

            return true;
        }
        return false;
    };
}
