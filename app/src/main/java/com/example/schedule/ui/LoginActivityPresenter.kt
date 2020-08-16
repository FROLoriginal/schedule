package com.example.schedule.ui

import android.content.Context
import com.example.schedule.API.ApiHolder
import com.example.schedule.API.NetworkService
import com.example.schedule.POJO.Response
import com.example.schedule.SQL.SQLScheduleEditor
import retrofit2.Call
import retrofit2.Callback

class LoginActivityPresenter(private val llv: LoginLoadingView,
                             private val context: Context) {

    private var responseCall: Call<Response?>? = null

    fun makeRequest(group: String) {

        responseCall = NetworkService.getInstance()
                .getJSONApi()
                .getSchedule(VERSION, ACCESS_TOKEN, group)

        responseCall!!.enqueue(object : Callback<Response?> {
            override fun onResponse(call: Call<Response?>,
                                    response: retrofit2.Response<Response?>) {
                val r = response.body()
                val jr = r!!.jsonResponse
                if (r.error != null) {
                    if (r.error!!.errorCode == ApiHolder.UNKNOWN_GROUP)
                        llv.showGroupIsNotExists()
                    else llv.showInternalError()

                } else if (jr != null) {
                    val tableName = group.replace("-", "")
                    val editor = SQLScheduleEditor(context, tableName, 1)
                    editor.fillDataBase(jr)
                    editor.close()
                    llv.startMainActivity()
                    llv.hideLoading(true)
                }
                llv.hideLoading(false)
            }

            override fun onFailure(call: Call<Response?>,
                                   t: Throwable) {
                if (!call.isCanceled) llv.showConnectionError()
                llv.hideLoading(false)
            }
        })
    }

    fun cancelRequest() {
        responseCall!!.cancel()
    }

    companion object {
        private const val VERSION = 1
        private const val ACCESS_TOKEN = "23a07a867ead26ba489f0b8dc7ab1c330f44a93dc72255e6bed322d5c5577fc2f0517665b03fcbfa794f795833"
    }

}