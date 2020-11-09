package com.example.schedule.ui.schedule

import android.app.TimePickerDialog
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

class ScheduleEditFragment internal constructor(private val listener: OnScheduleChangedListener?,
                                                private val lesson: SimpleScheduleModel?,
                                                private val pos: Int)
    : Fragment(), EditFragmentView, Toolbar.OnMenuItemClickListener {

    private lateinit var teacherEditText: EditText
    private lateinit var auditoryEditText: EditText
    private lateinit var subjectEditText: EditText
    private lateinit var toTime: TextView
    private lateinit var fromTime: TextView
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
        fromTime = root.findViewById(R.id.fromTextViewTime)
        toTime = root.findViewById(R.id.toTextViewTime)
        styleOfSubjectMACTV = root.findViewById(R.id.editStyleOfSybject)
        dayOfWeekSpinner = root.findViewById(R.id.dayOfWeekSpinner)

        val styles = resources.getStringArray(R.array.stylesOfSubject)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, styles)
        styleOfSubjectMACTV.setAdapter(adapter)

        fromTime.setOnClickListener(timePickerListener)
        toTime.setOnClickListener(timePickerListener)

        teacherEditText.setText(lesson?.teacher)
        auditoryEditText.setText(lesson?.auditory)
        subjectEditText.setText(lesson?.subject)
        toTime.text = if (lesson == null) null
        else Time.minutesToDisplayedTime(lesson.to)
        fromTime.text = if (lesson == null) null
        else Time.minutesToDisplayedTime(lesson.from)
        styleOfSubjectMACTV.setText(lesson?.prefixOfSubject)
        dayOfWeekSpinner.setSelection(if (lesson?.dayOfWeek == null) 0 else lesson.dayOfWeek - 1)

        activity = requireActivity() as MainActivity

        val pref = activity.getSharedPreferences(SQLManager.SHARED_PREF_DB_TABLE_NAME, Context.MODE_PRIVATE)
        val table = pref.getString(SQLManager.SHARED_PREF_TABLE_NAME_KEY, null)
        val editor = SQLScheduleEditor(requireContext(), table!!, SQLManager.VERSION)

        presenter = EditFragmentPresenter(this, editor)

        val fab: View = root.findViewById(R.id.fab_apply)
        fab.setOnClickListener(fabListener)

        val toolbar = root.findViewById<Toolbar>(R.id.toolbar)
        intention = arguments?.getString("key") ?: ""
        toolbar.title = intention
        toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
        toolbar.setOnMenuItemClickListener(this)
        ///activity.setSupportActionBar(toolbar) todo
        return root
    }


    private var totalMinTo = lesson?.to ?: -1
    private var totalMinFrom = lesson?.from ?: -1

    private val timePickerListener = View.OnClickListener {
        val time: Time = if (it.id == R.id.fromTextViewTime) {
            Time.displayedTimeToTime(fromTime.text.toString())
        } else Time.displayedTimeToTime(toTime.text.toString())

        TimePickerDialog(context, { _, hourOfDay, minute ->
            val totalMin = hourOfDay * 60 + minute
            if (it.id == R.id.fromTextViewTime) {
                totalMinFrom = totalMin
                fromTime.text = Time.minutesToDisplayedTime(totalMin)
            }
            if (it.id == R.id.toTextViewTime) {
                totalMinTo = totalMin
                toTime.text = Time.minutesToDisplayedTime(totalMin)
            }
        }, time.hour, time.minutes, true).show()
    }

    private val fabListener = View.OnClickListener {
        val teacher: String? = teacherEditText.text.toString()
        val auditory: String? = auditoryEditText.text.toString()
        val styleOfSubject: String = styleOfSubjectMACTV.text.toString()

        val subject = subjectEditText.text.toString()
        val dayOfWeek = dayOfWeekSpinner.selectedItem.toString()

        val model = SimpleScheduleModel().apply {
            this.subject = subject
            this.teacher = teacher
            this.auditory = auditory
            this.dayOfWeek = Time.strDayOfWeekToEUNum(dayOfWeek)
            this.prefixOfSubject = styleOfSubject
            this.from = totalMinFrom
            this.to = totalMinTo
            this.id = lesson?.id ?: 0
        }
        if (presenter.applyChanges(model)) {
            listener?.onScheduleIsChanged(pos, intention, model)
            parentFragmentManager.popBackStack()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.action_remove) {
            if (lesson != null) {
                presenter.removeLesson(lesson.id)
                listener?.onScheduleIsChanged(pos, IFragmentMovement.REMOVE_INTENTION)
            }
            parentFragmentManager.popBackStack()

        }
        return false
    }

    override fun onFieldIsNull() {
        Toast.makeText(context, "Необходимые поля не заполнены", Toast.LENGTH_SHORT).show()
    }


}
