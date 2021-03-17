package com.example.schedule.ui

interface LoginLoadingView : LoadingView {

    fun showGroupIfNotExist()
    fun showConnectionError()
    fun showInternalError()
    fun startMainActivity()
}
