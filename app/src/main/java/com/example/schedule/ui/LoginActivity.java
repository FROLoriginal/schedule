package com.example.schedule.ui;

import android.os.Bundle;
import android.view.View;

import com.example.schedule.API.NetworkService;
import com.example.schedule.POJO.JsonResponse;
import com.example.schedule.R;

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
                    .enqueue(new Callback<JsonResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<JsonResponse> call, @NonNull Response<JsonResponse> response) {

                            JsonResponse r = response.body();

                        }

                        @Override
                        public void onFailure(@NonNull Call<JsonResponse> call, @NonNull Throwable t) {
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
