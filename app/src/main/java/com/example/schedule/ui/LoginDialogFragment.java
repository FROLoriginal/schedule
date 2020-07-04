package com.example.schedule.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.schedule.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class LoginDialogFragment extends DialogFragment {

    private LoginActivity.RequestOperation operation;

    LoginDialogFragment(@NonNull LoginActivity.RequestOperation operation) {

        this.operation = operation;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.fragment_login, null));

        return builder.create();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        if (operation != null) operation.cancelRequest();

    }
}
