package com.example.schedule.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.schedule.SQL.SQLManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSharedPreferences(SQLManager.SHARED_PREFERENCES_TABLES, Context.MODE_PRIVATE).getAll().isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();

    }
}
