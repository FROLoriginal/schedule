package com.example.schedule.ui.schedule.editDialog

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.schedule.R
import com.example.schedule.SQL.SQLManager
import com.example.schedule.SQL.SQLScheduleEditor
import com.example.schedule.Util.TimeUtil
import com.example.schedule.Util.toast
import com.example.schedule.ui.MainActivity
import com.example.schedule.ui.schedule.LessonItem

class ScheduleEditFragment : Fragment(), EditFragmentView, Toolbar.OnMenuItemClickListener {

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

    private var pos: Int = -1
    private var lesson: LessonItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_schedule_edit, container, false)

        lesson = requireArguments().get(LESSON_BUNDLE) as LessonItem?
        pos = requireArguments().getInt(POS_BUNDLE)

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
        else TimeUtil.minutesToDisplayedTime(lesson!!.to)
        fromTime.text = if (lesson == null) null
        else TimeUtil.minutesToDisplayedTime(lesson!!.from)
        styleOfSubjectMACTV.setText(lesson?.typeOfSubject)
        dayOfWeekSpinner.setSelection(if (lesson?.dayOfWeek == null) 0 else lesson!!.dayOfWeek - 1)

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
        val time = if (it.id == R.id.fromTextViewTime) {
            TimeUtil.displayedTimeToTime(fromTime.text.toString())
        } else TimeUtil.displayedTimeToTime(toTime.text.toString())

        TimePickerDialog(context, { _, hourOfDay, minute ->
            val totalMin = hourOfDay * 60 + minute
            if (it.id == R.id.fromTextViewTime) {
                totalMinFrom = totalMin
                fromTime.text = TimeUtil.minutesToDisplayedTime(totalMin)
            }
            if (it.id == R.id.toTextViewTime) {
                totalMinTo = totalMin
                toTime.text = TimeUtil.minutesToDisplayedTime(totalMin)
            }
        }, time.hour, time.minute, true).show()
    }

    private val fabListener = View.OnClickListener {
        val teacher: String? = teacherEditText.text.toString()
        val auditory: String? = auditoryEditText.text.toString()
        val styleOfSubject: String = styleOfSubjectMACTV.text.toString()

        val subject = subjectEditText.text.toString()
        val dayOfWeek = dayOfWeekSpinner.selectedItem.toString()

        val model = LessonItem(TimeUtil.strDayOfWeekToEUNum(dayOfWeek),
                totalMinFrom,
                totalMinTo,
                teacher ?: "",
                auditory ?: "",
                subject, styleOfSubject,
                -1,
                id = lesson?.id ?: 0)


        if (presenter.applyChanges(model)) {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.action_remove) {
            if (lesson != null) {
                presenter.removeLesson(lesson!!.id)
            }
            parentFragmentManager.popBackStack()

        }
        return false
    }

    interface OnScheduleChangedListener {
        fun onScheduleChanged(pos: Int, intention: String, model: LessonItem? = null )
    }

    override fun onFieldIsNull() {
        requireContext().toast(R.string.requiredFieldIsNotFilled)
    }

    companion object {
        fun newInstance(pos: Int, item: LessonItem?): ScheduleEditFragment {
            return ScheduleEditFragment().apply {
                arguments = bundleOf(
                        Pair(POS_BUNDLE, pos)
                        , Pair(LESSON_BUNDLE, item)
                )
            }
        }

        private const val LESSON_BUNDLE = "LESSON_BUNDLE"
        private const val POS_BUNDLE = "POS_BUNDLE"
    }
}
