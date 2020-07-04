package com.example.schedule.ui;

import android.content.Context;
import android.content.Intent;
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
import com.example.schedule.R;
import com.example.schedule.SQL.SQLScheduleEditor;

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
        LoginDialogFragment fragment = new LoginDialogFragment(() -> responseCall.cancel());
        fragment.show(getSupportFragmentManager(), null);
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
                                SQLScheduleEditor editor = new SQLScheduleEditor(getApplicationContext(),tableName,1);
                                editor.fillDataBase(jr);
                                editor.close();
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

    interface RequestOperation{
        void cancelRequest();
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
            button.setEnabled(false);

            return true;
        }
        return false;
    };
}
