package com.example.schedule.ui.schedule

import com.example.schedule.Utils
import com.example.schedule.Utils.typeOfSubject

internal class SimpleScheduleModel {
    var id = 0
    var from: String? = null
    var to: String? = null
    var teacher: String? = null
    var auditory: String? = null
    var typeOfSubject: String? = null
    var subject: String? = null
    var styleOfSubject: String? = null
    var dayOfWeek = 0

    //starts from zero
    var counter = -1 //def val
    private var optionally = 0
    var isHeader = false

    constructor() {}
    constructor(model: SimpleScheduleModel) {
        from = model.from
        to = model.to
        teacher = model.teacher
        auditory = model.auditory
        typeOfSubject = model.typeOfSubject
        subject = model.subject
        styleOfSubject = model.styleOfSubject
        dayOfWeek = model.dayOfWeek
        counter = model.counter
        isHeader = model.isHeader
        id = model.id
        optionally = if (model.isOptionally()) 1 else 0
    }

    fun isOptionally(): Boolean {
        return optionally == 1
    }

    fun setOptionally(optionally: Int) {
        this.optionally = optionally
    }

    fun setStleOfSubject(styleOfSubject: String?) {
        this.styleOfSubject = styleOfSubject
    }

    val formattedTime: String
        get() = "$from - $to"

    val auditoryWithStyleOfSubject: String?
        get() {
            when (typeOfSubject(subject!!)) {
                Utils.SEMINAR -> return "Семинар, $auditory"
                Utils.LABORATORY_WORK -> return "Лаб. $auditory"
                Utils.LECTURE -> return "Лекция, $auditory"
                Utils.OTHER -> return auditory
            }
            return auditory
        }

    companion object {
        var isNumerator = false
        @JvmStatic
        fun equals(s1: SimpleScheduleModel, s2: SimpleScheduleModel): Boolean {
            return s1.counter == s2.counter && s1.counter >= 0 && s2.counter >= 0
        }

        @JvmStatic
        fun getNextLesson(data: List<SimpleScheduleModel>, position: Int): SimpleScheduleModel {
            var nextLes: SimpleScheduleModel
            var i = 0
            do {
                ++i
                nextLes = if (position + i < data.size) data[position + i] else SimpleScheduleModel()
            } while (equals(data[position], nextLes))
            return nextLes
        }

    }
}