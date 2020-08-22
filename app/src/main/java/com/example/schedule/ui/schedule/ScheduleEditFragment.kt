package com.example.schedule.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.schedule.R
import com.example.schedule.Utils

class ScheduleEditFragment internal constructor(private val fragment: Fragment,
                                                private val listener: ScheduleRecyclerViewAdapter.OnClickListener)
    : Fragment(),
        View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_schedule_edit, container, false)
        root.findViewById<View>(R.id.applyButton).setOnClickListener(this)

        return root
    }

    override fun onClick(v: View) {
        val view = requireView()

        val subject = view.findViewById<EditText>(R.id.editSubject).text.toString()
        val teacher: String? = view.findViewById<EditText>(R.id.editTeacher).text.toString()
        val auditory: String? = view.findViewById<EditText>(R.id.editAuditory).text.toString()
        val dayOfWeek = view.findViewById<Spinner>(R.id.dayOfWeekSpinner).selectedItem.toString()
        val typeOfSubject: String? = view.findViewById<MultiAutoCompleteTextView>(R.id.editTypeOfSybject).text.toString()
        val from: String = view.findViewById<EditText>(R.id.editTimeFrom).text.toString()
        val to: String = view.findViewById<EditText>(R.id.editTimeTo).text.toString()

        fragment
                .parentFragmentManager
                .beginTransaction()
                .hide(this)
                .show(fragment)
                .commit()
        listener.onClickToChangeLesson(
                SimpleScheduleModel().apply {
                    this.subject = subject
                    this.teacher = teacher
                    this.auditory = auditory
                    this.dayOfWeek = Utils.Time.convertStringDayOfWeekToEUNum(dayOfWeek)
                    this.typeOfSubject = typeOfSubject
                    this.from = from
                    this.to = to

                })
    }

}