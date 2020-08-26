package com.example.schedule.ui

import android.content.Context
import com.example.schedule.API.ApiHolder
import com.example.schedule.API.NetworkService
import com.example.schedule.POJO.JsonResponse
import com.example.schedule.POJO.Response
import com.example.schedule.POJO.Schedule
import com.example.schedule.SQL.SQLManager
import com.example.schedule.SQL.SQLScheduleEditor
import com.example.schedule.adapters.ScheduleTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONObject
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
             //   val r : Response? = response.body()
                val gson: Gson = GsonBuilder()
                        .registerTypeAdapter(Schedule::class.java, ScheduleTypeAdapter())
                        .create()

                val r : Response? = gson.fromJson(json, Response::class.java)
                var jr: JsonResponse? = r!!.jsonResponse

                if (r.error != null) {
                    if (r.error!!.errorCode == ApiHolder.UNKNOWN_GROUP)
                        llv.showGroupIsNotExists()
                    else llv.showInternalError()

                } else if (jr != null) {
                    val tableName = group.replace("-", "")
                    val editor = SQLScheduleEditor(context, tableName, SQLManager.VERSION)
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


        private const val json = "{\"response\":{\"schedule\":[[{\"from\":\"15:00\",\"to\":\"16:20\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Пр. Русский язык\",\"class\":\"463\"}}},{\"from\":\"16:30\",\"to\":\"17:50\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Пр. Русский язык\",\"class\":\"463\"}}}],[{\"from\":\"09:00\",\"to\":\"10:20\",\"type\":\"default\",\"object\":{\"subtype\":\"optionally\",\"subobject\":[{\"label\":\"toileth9\",\"subject\":\"Лаб. Компьютерный практикум по ИТ\",\"class\":\"ДК-3\"},{\"label\":\"q2qiphl7\",\"subject\":\"Лаб. Компьютерный практикум по моделированию\",\"class\":\"ДК-4\"}]}},{\"from\":\"10:30\",\"to\":\"11:50\",\"type\":\"default\",\"object\":{\"subtype\":\"optionally\",\"subobject\":[{\"label\":\"toileth9\",\"subject\":\"Лаб. Компьютерный практикум по ИТ\",\"class\":\"ДК-3\"},{\"label\":\"q2qiphl7\",\"subject\":\"Лаб. Компьютерный практикум по моделированию\",\"class\":\"ДК-4\"}]}},{\"from\":\"12:00\",\"to\":\"13:20\",\"type\":\"activity\",\"object\":{\"name\":\"Обед\"}},{\"from\":\"13:30\",\"to\":\"14:50\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Прикладная физическая культура\",\"teacher\":\"Мальченко А.Д.\",\"class\":\"ФОК РУДН\"}}}],[{\"from\":\"10:30\",\"to\":\"11:50\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Лекц. Математический анализ\",\"teacher\":\"Малых М.Д.\",\"class\":\"708\"}}},{\"from\":\"12:00\",\"to\":\"13:20\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Лекц. Математическая логика и теория алгоритмов\",\"teacher\":\"Маркова Е.В.\",\"class\":\"708\"}}},{\"from\":\"13:30\",\"to\":\"14:50\",\"type\":\"activity\",\"object\":{\"name\":\"Обед\"}},{\"from\":\"15:00\",\"to\":\"16:20\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Пр. Математическая логика и теория алгоритмов\",\"teacher\":\"Бегишев В.О.\",\"class\":\"210\"}}},{\"from\":\"16:30\",\"to\":\"17:50\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Пр. Математическая логика и теория алгоритмов\",\"teacher\":\"Бегишев В.О.\",\"class\":\"210\"}}}],[{\"from\":\"09:00\",\"to\":\"10:20\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Пр. История\",\"class\":\"210\"}}},{\"from\":\"10:30\",\"to\":\"11:50\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Пр. История\",\"class\":\"210\"}}},{\"from\":\"12:00\",\"to\":\"13:20\",\"type\":\"activity\",\"object\":{\"name\":\"Обед\"}},{\"from\":\"13:30\",\"to\":\"14:50\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Пр. Математический анализ\",\"teacher\":\"Тютюнник А.А.\",\"class\":\"210\"}}},{\"from\":\"15:00\",\"to\":\"16:20\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Пр. Математический анализ\",\"teacher\":\"Тютюнник А.А.\",\"class\":\"210\"}}}],[{\"from\":\"12:00\",\"to\":\"13:20\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Дистанционный курс Лекц. История\"}}},{\"from\":\"13:30\",\"to\":\"14:50\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Пр. Иностранный язык\",\"class\":\"535\"}}},{\"from\":\"15:00\",\"to\":\"16:20\",\"type\":\"changing\",\"object\":[{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Пр. Иностранный язык\",\"class\":\"535\"}},{\"subtype\":\"required\",\"subobject\":{\"subject\":\"ДПО \\\"Модуль переводчика\\\"\",\"class\":\"535\"}}]},{\"from\":\"16:30\",\"to\":\"17:50\",\"type\":\"default\",\"object\":{\"subtype\":\"required\",\"subobject\":{\"subject\":\"ДПО \\\"Модуль переводчика\\\"\",\"class\":\"535\"}}}],[{\"from\":\"12:00\",\"to\":\"13:20\",\"type\":\"changing\",\"object\":[{\"subtype\":\"pass\"},{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Пр. Русский язык\",\"class\":\"463\"}}]},{\"from\":\"13:30\",\"to\":\"14:50\",\"type\":\"changing\",\"object\":[{\"subtype\":\"pass\"},{\"subtype\":\"required\",\"subobject\":{\"subject\":\"Пр. Русский язык\",\"class\":\"463\"}}]}]],\"labels\":[[{\"label\":\"toileth9\",\"name\":\"Компьютерный практикум по ИТ\"},{\"label\":\"q2qiphl7\",\"name\":\"Компьютерные практикум по моделированию\"}]]}}"
    }

}
