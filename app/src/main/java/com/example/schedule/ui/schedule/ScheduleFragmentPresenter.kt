package com.example.schedule.ui.schedule

import com.example.schedule.Util.LessonItemUtils
import com.example.schedule.Util.Time
import com.example.schedule.Util.TimeUtil

class ScheduleFragmentPresenter {

    fun addLessonToSchedule(list: ArrayList<RecyclerViewItem>,
                            lesson: LessonItem) {

        list.add(lesson)
        prepare(list)
    }

    fun changeLesson(list: ArrayList<RecyclerViewItem>,
                     pos: Int,
                     newLesson: LessonItem) {
        if (pos < -1 || pos >= list.size) throw IndexOutOfBoundsException("Position $pos must be less then list.size and >= 0")
        list[pos] = newLesson
        prepare(list)
    }

    //Remove lesson from prepared list to show list
    fun removeItem(pos: Int, list: ArrayList<RecyclerViewItem>) {

        fun removeAndNotify(pos: Int) {
            list.removeAt(pos) // remove the header
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
        if (list.isNotEmpty()) prepare(list)

    }

    fun prepare(list: ArrayList<RecyclerViewItem>) {
        removeHeaders(list)
        (list as ArrayList<LessonItem>).sort()
        setCounters(list)
        addHeaders(list)
    }

    private fun ArrayList<LessonItem>.sort() {
        sortWith(compareBy({ it.dayOfWeek }, { it.from }, { it.to }))
    }


    private fun setCounters(list: ArrayList<LessonItem>) {

        var counter = 0
        for (i in 0 until list.size - 1) {
            val l1: LessonItem = list[i]
            val l2: LessonItem = list[i + 1]

            l1.counter = counter

            when {
                l1.dayOfWeek != l2.dayOfWeek -> counter = 0
                l1.from != l2.from && l1.to != l2.to -> counter++
            }
        }
        list.last().counter = counter
    }

    private fun removeHeaders(list: ArrayList<RecyclerViewItem>) {

        var i = 0
        while (i < list.size) {
            if (list[i].isHeader) list.removeAt(i)
            i++
        }
    }

    private fun addHeaders(list: ArrayList<RecyclerViewItem>) {

        list.add(0, HeaderItem(list[0].dayOfWeek))

        var i = 1
        while (i < list.size - 1) {
            if (list[i].dayOfWeek != list[i + 1].dayOfWeek) {
                list.add(i + 1, HeaderItem(list[i + 1].dayOfWeek))
            }
            i++
        }
    }
}
