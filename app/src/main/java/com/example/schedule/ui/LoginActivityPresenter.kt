package com.example.schedule.ui

import android.content.Context
import com.example.schedule.API.ApiHolder
import com.example.schedule.API.NetworkService
import com.example.schedule.POJO.JsonResponse
import com.example.schedule.POJO.Response
import com.example.schedule.SQL.SQLManager
import com.example.schedule.SQL.SQLScheduleEditor
import retrofit2.Call
import retrofit2.Callback

class LoginActivityPresenter(private val llv: LoginLoadingView,
                             private val context: Context) {

    private lateinit var responseCall: Call<Response>

    fun makeRequest(group: String) {

        responseCall = NetworkService
                .getInstance()
                .getJSONApi()
                .getSchedule(VERSION, ACCESS_TOKEN, group)

        responseCall.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>,
                                    response: retrofit2.Response<Response>) {
                val r: Response = response.body()!!

                r.jsonResponse?.let {
                    createTable(group, it)
                    llv.startMainActivity()
                    llv.hideLoading(false)
                    return
                }
                r.error?.run {
                    if (errorCode == ApiHolder.UNKNOWN_GROUP) llv.showGroupIfNotExist()
                    else llv.showInternalError()
                    llv.hideLoading(true)
                }
            }

            override fun onFailure(call: Call<Response?>,
                                   t: Throwable) {
                if (!call.isCanceled) llv.showConnectionError()
                llv.hideLoading(true)
            }
        })
    }

    private fun createTable(group: String, jr: JsonResponse) {
        SQLScheduleEditor(context,
                group.formatToTableName(),
                SQLManager.VERSION)
                .use { it.fillDataBase(jr) }
    }

    private fun String.formatToTableName() = this.replace("-", "")

    fun cancelRequest() {
        if(::responseCall.isInitialized) {
            responseCall.cancel()
        }else throw NullPointerException("Unable to cancel a request that has not been sent")
    }

    companion object {
        private const val VERSION = 1
        private const val ACCESS_TOKEN = "23a07a867ead26ba489f0b8dc7ab1c330f44a93dc72255e6bed322d5c5577fc2f0517665b03fcbfa794f795833"
    }

}
