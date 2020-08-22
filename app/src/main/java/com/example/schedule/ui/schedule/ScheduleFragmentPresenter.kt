package com.example.schedule.ui.schedule

import com.example.schedule.Utils

class ScheduleFragmentPresenter(private val sv: ScheduleRecyclerView?) {

    fun addLessonToSchedule(list: MutableList<SimpleScheduleModel>,
                            lesson: SimpleScheduleModel) {

        list.add(lesson)
        prepareData(list)
        sv?.onItemAdded()
    }

    fun changeLesson(list : MutableList<SimpleScheduleModel>,
                     pos : Int,
                     newLesson : SimpleScheduleModel){

        list[pos] = newLesson
        sv?.onItemChanged(pos)
    }

    //Remove lesson from prepared to show list
    fun removeLesson(pos: Int, list: MutableList<SimpleScheduleModel>) {
        if (list[pos - 1].isHeader && list[pos + 1].isHeader) {
            list.removeAt(pos)
            list.removeAt(pos - 1) //Remove the header
            sv?.onItemRemoved(pos - 1)

        } else {
            list.removeAt(pos)
        }
        setCounters(list)
        sv?.onItemRemoved(pos)

    }

    fun prepareData(list: MutableList<SimpleScheduleModel>){
        list.sort()
        setCounters(list)
        addHeaders(list)
    }

    private fun MutableList<SimpleScheduleModel>.sort() {
        sortWith(compareBy({ it.dayOfWeek }, {
            when (it.from!!.length) {
                4 -> "0" + it.from
                else -> it.from
            }
        }, {
            when (it.to!!.length) {
                4 -> "0" + it.to
                else -> it.to
            }
        }))
    }

    private fun setCounters(list: MutableList<SimpleScheduleModel>) {

        var counter = 0
        for (i in 0 until list.size - 1) {
            val l1: SimpleScheduleModel = list[i]
            val l2: SimpleScheduleModel = list[i + 1]

            l1.counter = counter
            if (!SimpleScheduleModel.isOneDayLessons(l1, l2)) counter = 0
            else if (Utils.Time.isTimeIntersect(
                            Utils.Time(l1.from, l1.to),
                            Utils.Time(l2.from, l2.to))) l2.counter = counter
            else counter++

        }

    }

    private fun addHeaders(list: MutableList<SimpleScheduleModel>) {

        val model = SimpleScheduleModel()
        model.isHeader = true
        model.dayOfWeek = list[0].dayOfWeek
        list.add(0, SimpleScheduleModel(model))

        var i = 1
        while (i < list.size - 1) {

            if (!SimpleScheduleModel.isOneDayLessons(list[i], list[i + 1])) {

                model.dayOfWeek = list[i + 1].dayOfWeek
                list.add(i + 1, SimpleScheduleModel(model))
            }
            i++
        }

    }
}
