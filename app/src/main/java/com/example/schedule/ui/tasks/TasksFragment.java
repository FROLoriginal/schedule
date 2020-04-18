package com.example.schedule.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schedule.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class TasksFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        final TextView textView = root.findViewById(R.id.text_dashboard);

        return root;
    }
}
