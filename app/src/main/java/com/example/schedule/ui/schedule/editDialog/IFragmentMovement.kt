package com.example.schedule.ui.schedule.editDialog

import androidx.fragment.app.Fragment

interface IFragmentMovement{

    /**
     * @param to the way move to
     * @param intention the reason of moving
     **/

    fun onMove(from: Fragment,to: Fragment, intention: String, name: String)

    companion object {
        val EDIT_INTENTION: String
            get() = "Редактирование"

        val CREATE_INTENTION: String
            get() = "Создание"

        val REMOVE_INTENTION: String
            get() = "Удаление"
    }
}
