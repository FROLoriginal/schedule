package com.example.schedule;

import android.os.Bundle;
import android.view.View;

import com.example.schedule.json.NetworkService;
import com.example.schedule.json.Schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClick(View v) {


        new Thread(() -> {
            NetworkService.getInstance()
                    .getJSONApi()
                    .getPostWithID()
                    .enqueue(new Callback<Schedule>() {
                        @Override
                        public void onResponse(@NonNull Call<Schedule> call, @NonNull Response<Schedule> response) {
                            // String post = response.body();
                            //System.out.println(post + "''''''''''''''''''''''''''''''''''''");
                        }

                        @Override
                        public void onFailure(@NonNull Call<Schedule> call, @NonNull Throwable t) {
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
