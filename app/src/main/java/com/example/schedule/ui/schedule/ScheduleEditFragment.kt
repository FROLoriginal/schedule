package com.example.schedule.ui.schedule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.schedule.R
import com.example.schedule.SQL.SQLManager
import com.example.schedule.SQL.SQLScheduleEditor
import com.example.schedule.Util.Time
import com.example.schedule.ui.MainActivity
import com.example.schedule.viewModel.SimpleScheduleModel
import java.util.concurrent.TimeUnit


class ScheduleEditFragment internal constructor(private val listener: OnScheduleChangedListener?,
                                                private val lesson: SimpleScheduleModel?,
                                                private val pos: Int)
    : Fragment(), EditFragmentView, Toolbar.OnMenuItemClickListener {

    private lateinit var teacherEditText: EditText
    private lateinit var auditoryEditText: EditText
    private lateinit var subjectEditText: EditText
    private lateinit var toEditText: EditText
    private lateinit var fromEditText: EditText
    private lateinit var styleOfSubjectMACTV: MultiAutoCompleteTextView
    private lateinit var dayOfWeekSpinner: Spinner

    private lateinit var activity: MainActivity
    private lateinit var presenter: EditFragmentPresenter

    private lateinit var intention: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_schedule_edit, container, false)

        teacherEditText = root.findViewById(R.id.editTeacher)
        auditoryEditText = root.findViewById(R.id.editAuditory)
        subjectEditText = root.findViewById(R.id.editSubject)
        toEditText = root.findViewById(R.id.editTimeTo)
        fromEditText = root.findViewById(R.id.editTimeFrom)
        styleOfSubjectMACTV = root.findViewById(R.id.editStyleOfSybject)
        dayOfWeekSpinner = root.findViewById(R.id.dayOfWeekSpinner)

        val styles = resources.getStringArray(R.array.stylesOfSubject)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, styles)

        styleOfSubjectMACTV.setAdapter(adapter)

        teacherEditText.setText(lesson?.teacher)
        auditoryEditText.setText(lesson?.auditory)
        subjectEditText.setText(lesson?.subject)
        toEditText.setText(
                if (lesson == null) null
                else Time.minutesToDisplayedTime(lesson.to))
        fromEditText.setText(
                if (lesson == null) null
                else Time.minutesToDisplayedTime(lesson.from))
        styleOfSubjectMACTV.setText(lesson?.styleOfSubject)
        dayOfWeekSpinner.setSelection(if (lesson?.dayOfWeek == null) 0 else lesson.dayOfWeek - 1)

        activity = requireActivity() as MainActivity
        presenter = EditFragmentPresenter(this)

        val toolbar = root.findViewById<Toolbar>(R.id.toolbar)
        intention = arguments?.getString("key") ?: ""
        toolbar.title = intention
        toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
        toolbar.setOnMenuItemClickListener(this)
        ///activity.setSupportActionBar(toolbar) todo
        return root
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.action_apply) {

            val teacher: String? = teacherEditText.text.toString()
            val auditory: String? = auditoryEditText.text.toString()
            val styleOfSubject: String = styleOfSubjectMACTV.text.toString()

            val subject = subjectEditText.text.toString()
            val dayOfWeek = dayOfWeekSpinner.selectedItem.toString()

            val fromStr = fromEditText.text.toString()
            val toStr = toEditText.text.toString()

            var from = -1
            var to = -1

            if (fromStr.isNotEmpty() && toStr.isNotEmpty()) {

                val tFrom = Time.displayedTimeToTime(fromStr)
                val tTo = Time.displayedTimeToTime(toStr)

                from = TimeUnit.HOURS.toMinutes(tFrom.hour.toLong()).toInt() + tFrom.minutes
                to = TimeUnit.HOURS.toMinutes(tTo.hour.toLong()).toInt() + tTo.minutes
            }

            val model = SimpleScheduleModel().apply {
                this.subject = subject
                this.teacher = teacher
                this.auditory = auditory
                this.dayOfWeek = Time.strDayOfWeekToEUNum(dayOfWeek)
                this.styleOfSubject = styleOfSubject
                this.from = from
                this.to = to
                this.id = lesson?.id ?: 0
            }

            val pref = activity.getSharedPreferences(SQLManager.SHARED_PREF_DB_TABLE_NAME, Context.MODE_PRIVATE)
            val table = pref.getString(SQLManager.SHARED_PREF_TABLE_NAME_KEY, null)
            val editor = SQLScheduleEditor(requireContext(), table!!, SQLManager.VERSION)

            val isChanged = presenter.applyChanges(model, editor)
            if (isChanged) {
                listener?.onScheduleIsChanged(model, pos, intention)
                parentFragmentManager.popBackStack()
            }
            return isChanged
        }
        return false
    }

    override fun onFieldIsNull() {
        Toast.makeText(context, "Необходимые поля не заполнены", Toast.LENGTH_SHORT).show()
    }


}
