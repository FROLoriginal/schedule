package com.example.schedule.viewModel

import com.example.schedule.Util.Time

class SimpleScheduleModel {
    var id = 0
    var from = 0
    var to = 0
    var teacher: String? = null
    var auditory: String? = null
    var subject: String? = null
    var prefixOfSubject = ""

    //Day of week starts from 1, zero is `header` value
    var dayOfWeek = 0

    //starts from zero
    var counter = -1 //def val
    private var optionally = 0
    var isHeader = false

    constructor()
    constructor(model: SimpleScheduleModel) {
        from = model.from
        to = model.to
        teacher = model.teacher
        auditory = model.auditory
        subject = model.subject
        prefixOfSubject = model.prefixOfSubject
        dayOfWeek = model.dayOfWeek
        counter = model.counter
        isHeader = model.isHeader
        id = model.id
        optionally = if (model.isOptionally()) 1 else 0
    }

    fun isOptionally() = optionally == 1

    fun setOptionally(optionally: Boolean) {
        this.optionally = if (optionally) 1 else 0
    }

    val formattedTime: String
        get() = "${Time.minutesToDisplayedTime(from)} - ${Time.minutesToDisplayedTime(to)}"

    val auditoryWithStyleOfSubject: String
        get() {
            return if (prefixOfSubject.isEmpty() && auditory.isNullOrEmpty()) {
                ""
            } else if (prefixOfSubject.isEmpty()) {
                auditory!!
            } else if (auditory.isNullOrEmpty()) {
                prefixOfSubject
            } else "$prefixOfSubject, $auditory"
        }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        else if (other !is SimpleScheduleModel)
            throw IllegalArgumentException("Parameter ${other.javaClass} must be a SimpleScheduleModel")
        return this.counter == other.counter && this.counter >= 0
    }

    companion object {

        fun getNextLesson(data: List<SimpleScheduleModel>, position: Int): SimpleScheduleModel {
            if (position >= data.size)
                throw IndexOutOfBoundsException("Data size ${data.size} must be more then $position position")
            if (position > -1) {
                for (i in position until data.size) {
                    when {
                        i == data.lastIndex ->
                            throw IllegalArgumentException("It is the last lesson. Use isNextLessonExists(data,pos)")
                        data[i + 1].isHeader ->
                            throw IllegalArgumentException("It is the last lesson (next element is header). Use isNextLessonExists(data,pos)")
                        data[position] != data[i] -> return data[i]
                    }

                }
                throw IllegalArgumentException("Lesson is not exists or it is header. Use isNextLessonExists(data,pos)")
            } else throw IllegalArgumentException("Position $position must be positive value")
        }

        fun SimpleScheduleModel.isOneDayLessons(target: SimpleScheduleModel) = this.dayOfWeek == target.dayOfWeek


        fun isNextLessonExists(data: List<SimpleScheduleModel>, position: Int): Boolean {
            if (position >= data.size)
                throw IndexOutOfBoundsException("Data size ${data.size} must be more then $position position")
            if (position > -1) {
                for (i in position until data.size) {
                    when {
                        i == data.lastIndex -> return false
                        data[i + 1].isHeader -> return false
                        data[position] != data[i] -> return true
                    }
                }
                return false
            } else throw IllegalArgumentException("Position $position must be positive value")
        }
    }
}
