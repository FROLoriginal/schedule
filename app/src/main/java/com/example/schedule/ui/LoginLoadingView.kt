package com.example.schedule.ui

interface LoginLoadingView : LoadingView {

    fun showGroupIsNotExists()
    fun showConnectionError()
    fun showInternalError()
    fun startMainActivity()
}
