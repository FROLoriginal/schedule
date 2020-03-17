package com.example.schedule.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.schedule.API.NetworkService;
import com.example.schedule.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        text = findViewById(R.id.group);
    }

    private static int VERSION = 1;
    private static String ACCESS_TOKEN = "23a07a867ead26ba489f0b8dc7ab1c330f44a93dc72255e6bed322d5c5577fc2f0517665b03fcbfa794f795833";


    public void onClick(View v) {

        new Thread(() -> {
            NetworkService.getInstance()
                    .getJSONApi().
                    getSchedule(VERSION, ACCESS_TOKEN, text.getText().toString())
                    .enqueue(new Callback<com.example.schedule.POJO.OK_POJO.Response>() {
                        @Override
                        public void onResponse(@NonNull Call<com.example.schedule.POJO.OK_POJO.Response> call,
                                               @NonNull Response<com.example.schedule.POJO.OK_POJO.Response> response) {

                            com.example.schedule.POJO.OK_POJO.Response r = response.body();
                            System.out.println(r.getObj().getJsonResponse().getSchedule().get(4).getObject().get(3).getSubobject().get(0).getSubject());
                        }

                        @Override
                        public void onFailure(@NonNull Call<com.example.schedule.POJO.OK_POJO.Response> call,
                                              @NonNull Throwable t) {
                            System.out.println(call.request().toString());
                            t.printStackTrace();
                        }
                    });
        }).start();
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
        // finish();
    }
}
