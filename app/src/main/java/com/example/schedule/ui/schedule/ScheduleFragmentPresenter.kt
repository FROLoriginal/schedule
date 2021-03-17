package com.example.schedule.ui.schedule

import com.example.schedule.Util.Time
import com.example.schedule.viewModel.SimpleScheduleModel
import com.example.schedule.viewModel.SimpleScheduleModel.Companion.isOneDayLessons

class ScheduleFragmentPresenter(private val sv: ScheduleRecyclerView?) {

    fun addLessonToSchedule(list: ArrayList<SimpleScheduleModel>,
                            lesson: SimpleScheduleModel) {

        list.add(lesson)
        prepareData(list)
        sv?.onItemAdded()
    }

    fun changeLesson(list: ArrayList<SimpleScheduleModel>,
                     pos: Int,
                     newLesson: SimpleScheduleModel) {
        if (pos < -1 || pos >= list.size) throw IndexOutOfBoundsException("Position $pos must be less then list.size and >= 0")
        list[pos] = newLesson
        prepareData(list)
        sv?.onItemChanged()
    }

    //Remove lesson from prepared list to show list
    fun removeLesson(pos: Int, list: ArrayList<SimpleScheduleModel>) {

        fun removeAndNotify(pos: Int) {
            list.removeAt(pos) // remove the header
            sv?.onItemRemoved(pos)
        }
        if (pos < -1 || pos >= list.size) throw IndexOutOfBoundsException("Position $pos must be less then list.size and >= 0")

        list.removeAt(pos)
        if (pos != list.size) {
            if (list[pos - 1].isHeader && list[pos].isHeader) {
                removeAndNotify(pos - 1)
            }
        } else {
            if (list[pos - 1].isHeader) {
                removeAndNotify(pos - 1)
            }
        }
        if (list.isNotEmpty()) prepareData(list)
        sv?.onItemRemoved(pos)

    }

    fun prepareData(list: ArrayList<SimpleScheduleModel>) {
        removeHeaders(list)
        list.sort()
        setCounters(list)
        addHeaders(list)
    }

    private fun MutableList<SimpleScheduleModel>.sort() {
        sortWith(compareBy({ it.dayOfWeek }, { it.from }, { it.to }))
    }

    private fun setCounters(list: ArrayList<SimpleScheduleModel>) {

        var counter = 0
        for (i in 0 until list.size - 1) {
            val l1: SimpleScheduleModel = list[i]
            val l2: SimpleScheduleModel = list[i + 1]

            l1.counter = counter

            if (!l1.isOneDayLessons(l2))
                counter = 0
            else if (Time.isTimeIntersect(
                            Time.Lesson(Time(l1.from), Time(l1.to)),
                            Time.Lesson(Time(l2.from), Time(l2.to)))) {
                l1.setOptionally(true)
                l2.setOptionally(true)
                l2.counter = counter
            } else {
                if (i == list.size - 2) {
                    l2.setOptionally(false)
                    l2.counter = counter
                } else counter++
            }

        }


    }

    private fun removeHeaders(list: ArrayList<SimpleScheduleModel>) {

        var i = 0
        while (i < list.size) {
            if (list[i].isHeader) list.removeAt(i)
            i++
        }

    }

    private fun addHeaders(list: ArrayList<SimpleScheduleModel>) {

        val model = SimpleScheduleModel()
        model.isHeader = true
        model.dayOfWeek = list[0].dayOfWeek
        list.add(0, SimpleScheduleModel(model))

        var i = 1
        while (i < list.size - 1) {

            if (!list[i].isOneDayLessons(list[i + 1])) {

                model.dayOfWeek = list[i + 1].dayOfWeek
                list.add(i + 1, SimpleScheduleModel(model))
            }
            i++
        }

    }
}
