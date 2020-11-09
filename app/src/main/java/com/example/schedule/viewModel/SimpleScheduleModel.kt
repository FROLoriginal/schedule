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


    fun setOptionally(optionally: Int) {
        this.optionally = optionally
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

        @JvmStatic
        fun getNextLesson(data: List<SimpleScheduleModel>, position: Int): SimpleScheduleModel {
            if (position >= data.size)
                throw IndexOutOfBoundsException("Data size ${data.size} must be more then $position position")
            if (position > -1) {
                var nextLes: SimpleScheduleModel
                var i = 0
                do {
                    nextLes = if (position + i < data.size) data[position + i] else SimpleScheduleModel()
                    i++
                } while (data[position] == nextLes)
                return nextLes
            } else throw IllegalArgumentException("Position $position must be positive value")
        }

        @JvmStatic
        fun isOneDayLessons(target1: SimpleScheduleModel,
                            target2: SimpleScheduleModel)
                : Boolean = target1.dayOfWeek == target2.dayOfWeek

    }
}
