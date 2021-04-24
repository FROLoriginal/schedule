package com.example.schedule.ui.login

interface LoginLoadingView : LoadingView {

    fun showGroupIfNotExist()
    fun showConnectionError()
    fun showInternalError()
    fun startMainActivity()
}
