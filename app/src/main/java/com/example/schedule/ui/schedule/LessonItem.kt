package com.example.schedule.ui.schedule

import android.os.Parcel
import android.os.Parcelable
import com.example.schedule.Util.LessonStatus
import com.example.schedule.Util.Time
import com.example.schedule.Util.TimeUtil
import java.io.Serializable
import java.util.*

class LessonItem(override var dayOfWeek: Int,
                 val from: Int,
                 val to: Int,
                 val teacher: String?,
                 val auditory: String?,
                 val subject: String?,
                 val typeOfSubject: String,
                 var counter: Int,    //Number of current lesson. It starts at zero
                 val id: Int

) : RecyclerViewItem(), Serializable {

    override var isHeader: Boolean = false

    fun status() = LessonStatus.getInstance(this)

    val formattedTime: String
        get() = "${TimeUtil.minutesToDisplayedTime(from)} - ${TimeUtil.minutesToDisplayedTime(to)}"

    val auditoryWithTypeOfSubject: String
        get() {
            return if (typeOfSubject.isEmpty() && auditory.isNullOrEmpty()) ""
            else if (typeOfSubject.isEmpty()) auditory!!
            else if (auditory.isNullOrEmpty()) typeOfSubject
            else "$typeOfSubject, $auditory"
        }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        else if (other !is LessonItem)
            throw IllegalArgumentException("Parameter ${other.javaClass} must be a LessonItem")
        return this.counter == other.counter && this.counter >= 0
    }

}
