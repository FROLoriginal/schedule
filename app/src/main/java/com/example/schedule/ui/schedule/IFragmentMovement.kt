package com.example.schedule.ui.schedule

import androidx.fragment.app.Fragment

interface IFragmentMovement{

    fun onMove(fragment : Fragment, intention : String)

    companion object {
        val EDIT_INTENTION: String
            get() = "Редактирование"

        val CREATE_INTENTION: String
            get() = "Создание"
    }
}
