package com.example.schedule.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schedule.R;
import com.example.schedule.Utils;

import java.util.List;

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

    //todo сделать сортировку после добавления
    private void addLessonToList(List<SimpleScheduleModel> list, SimpleScheduleModel lesson) {

        if (!lesson.isHeader()) {

            for (int i = 0; i < list.size(); i++) {

                SimpleScheduleModel les = list.get(i);

                if (les.getDayOfWeek() == lesson.getDayOfWeek()) {

                    String lessonTo = lesson.getTo();
                    SimpleScheduleModel nextLes = SimpleScheduleModel.getNextLesson(list, i);

                    if (Utils.Time.timeToMinutes(lessonTo) <
                            Utils.Time.timeToMinutes(nextLes.getFrom())) {
                        lesson.setCounter(nextLes.getCounter() + 1);
                        list.add(i, lesson);

                    } else if (Utils.Time.isTimeIntersect(les.getFrom(), les.getTo(),
                            lesson.getFrom(), lessonTo)) {
                        lesson.setCounter(les.getCounter());
                        list.add(i, lesson);
                    }
                }
            }
        }
    }
}
