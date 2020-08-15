package com.example.schedule.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schedule.API.ApiHolder
import com.example.schedule.API.NetworkService.Companion.getInstance
import com.example.schedule.POJO.OK_POJO.Response
import com.example.schedule.R
import com.example.schedule.SQL.SQLScheduleEditor
import retrofit2.Call
import retrofit2.Callback

class LoginActivity : AppCompatActivity() {
    private var text: EditText? = null
    private var button: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        text = findViewById(R.id.group)
        button = findViewById(R.id.getScheduleButton)
        button!!.isEnabled = false
        text!!.addTextChangedListener(textChangedListener)
        text!!.setOnEditorActionListener(inputGroup)
    }

    private var responseCall: Call<Response?>? = null

    fun onClick(v: View?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(text!!.windowToken, 0)
        val fragment = LoginDialogFragment(object : RequestOperation {
            override fun cancelRequest() {
                responseCall!!.cancel()
            }
        })
        fragment.show(supportFragmentManager, null)
        val editText = text!!.text.toString()
        responseCall = getInstance()
                .getJSONApi()
                .getSchedule(VERSION, ACCESS_TOKEN, editText)
        Thread(Runnable {
            responseCall!!
                    .enqueue(object : Callback<Response?> {
                        override fun onResponse(call: Call<Response?>,
                                                response: retrofit2.Response<Response?>) {
                            val r = response.body()
                            val jr = r!!.jsonResponse
                            if (r.error != null) {
                                when (r.error!!.errorCode) {
                                    ApiHolder.UNKNOWN_GROUP -> {
                                        Toast.makeText(applicationContext, R.string.unknownGroup, Toast.LENGTH_LONG).show()
                                        text!!.setText("")
                                    }
                                    else -> Toast.makeText(applicationContext, R.string.internalErrorOccurred, Toast.LENGTH_LONG).show()
                                }
                            } else if (jr != null) {
                                val tableName = editText.replace("-", "")
                                val editor = SQLScheduleEditor(applicationContext, tableName, 1)
                                editor.fillDataBase(jr)
                                editor.close()
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                fragment.dismiss()
                                finish()
                            }
                            fragment.dismiss()
                        }

                        override fun onFailure(call: Call<Response?>,
                                               t: Throwable) {
                            if (!call.isCanceled) {
                                Toast.makeText(applicationContext, R.string.connectionError, Toast.LENGTH_LONG).show()
                            }
                            fragment.dismiss()
                        }
                    })
        }).start()
    }

    interface RequestOperation {
        fun cancelRequest()
    }

    private val textChangedListener: TextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            button!!.isEnabled = s.toString() != ""
        }
    }
    private val inputGroup = OnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
        if (actionId == EditorInfo.IME_ACTION_DONE && button!!.isEnabled) {
            onClick(null)
            button!!.isEnabled = false
            true
        }else false
    }

    companion object {
        private const val VERSION = 1
        private const val ACCESS_TOKEN = "23a07a867ead26ba489f0b8dc7ab1c330f44a93dc72255e6bed322d5c5577fc2f0517665b03fcbfa794f795833"
    }
}
