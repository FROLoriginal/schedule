package com.example.schedule.ui.schedule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.schedule.R
import com.example.schedule.SQL.SQLManager
import com.example.schedule.SQL.SQLScheduleEditor
import com.example.schedule.Utils
import com.example.schedule.ui.MainActivity

class ScheduleEditFragment internal constructor(private val listener: ScheduleRecyclerViewAdapter.OnClickListener,
                                                private val lesson: SimpleScheduleModel)
    : Fragment(), EditFragmentView, Toolbar.OnMenuItemClickListener {

    private lateinit var teacherEditText: EditText
    private lateinit var auditoryEditText: EditText
    private lateinit var subjectEditText: EditText
    private lateinit var toEditText: EditText
    private lateinit var fromEditText: EditText
    private lateinit var styleOfSubjectMACTV: MultiAutoCompleteTextView
    private lateinit var dayOfWeekSpinner: Spinner

    private lateinit var activity: MainActivity


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

        teacherEditText.setText(lesson.teacher)
        auditoryEditText.setText(lesson.auditory)
        subjectEditText.setText(lesson.subject)
        toEditText.setText(lesson.to)
        fromEditText.setText(lesson.from)
        styleOfSubjectMACTV.setText(lesson.styleOfSubject)
        dayOfWeekSpinner.setSelection(lesson.dayOfWeek - 1)

        activity = requireActivity() as MainActivity

        val toolbar = root.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = arguments?.getString("edit") ?: ""
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
            val from = fromEditText.text.toString()
            val to = toEditText.text.toString()

            val model = SimpleScheduleModel().apply {
                this.subject = subject
                this.teacher = teacher
                this.auditory = auditory
                this.dayOfWeek = Utils.Time.convertStringDayOfWeekToEUNum(dayOfWeek)
                this.styleOfSubject = styleOfSubject
                this.from = from
                this.to = to
                this.id = lesson.id
            }
            val pref = activity.getSharedPreferences(SQLManager.SHARED_PREF_DB_TABLE_NAME, Context.MODE_PRIVATE)
            val table = pref.getString(SQLManager.SHARED_PREF_TABLE_NAME_KEY, null)
            val editor = SQLScheduleEditor(context, table, SQLManager.VERSION)
            EditFragmentPresenter(this).applyChanges(model, editor)
            listener.onClickToChangeLesson(model)
            parentFragmentManager.popBackStack()
            return true
        }
        return false
    }

    override fun onFieldIsNull() {
        Toast.makeText(context, "Необходимые поля не заполнены", Toast.LENGTH_SHORT).show()
    }


}
