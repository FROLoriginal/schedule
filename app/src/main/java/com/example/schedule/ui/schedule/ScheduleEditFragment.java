package com.example.schedule.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schedule.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ScheduleEditFragment extends Fragment {

    private Fragment fragment;

    ScheduleEditFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_schedule_edit, container, false);

        root.setOnClickListener(p -> {
            FragmentTransaction ft = fragment
                    .getParentFragmentManager()
                    .beginTransaction();
            ft.hide(this);
            ft.show(fragment);
            ft.commit();
        });

        return root;
    }
}
