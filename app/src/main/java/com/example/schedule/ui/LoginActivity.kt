package com.example.schedule.ui

import android.content.Context
import android.content.DialogInterface
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
import com.example.schedule.R

class LoginActivity : AppCompatActivity(), LoginLoadingView {

    private var text: EditText? = null
    private var button: Button? = null
    private lateinit var presenter: LoginActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        text = findViewById(R.id.group)
        button = findViewById(R.id.getScheduleButton)
        button!!.isEnabled = false
        text!!.addTextChangedListener(textChangedListener)
        text!!.setOnEditorActionListener(inputGroup)
        presenter = LoginActivityPresenter(this,applicationContext)
    }

    fun onClick(v: View?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(text!!.windowToken, 0)
        showLoading()
        val editText = text!!.text.toString()
        Thread(Runnable{presenter.makeRequest(editText)}).start()
    }

    override fun showGroupIsNotExists() {
        Toast.makeText(applicationContext, R.string.unknownGroup, Toast.LENGTH_LONG).show()
        text!!.setText("")
    }

    override fun showConnectionError() {
        Toast.makeText(applicationContext, R.string.connectionError, Toast.LENGTH_LONG).show()
    }

    override fun showInternalError() {
        Toast.makeText(applicationContext, R.string.internalErrorOccurred, Toast.LENGTH_LONG).show()
    }

    private val fragment = LoginDialogFragment(DialogInterface.OnCancelListener{
        presenter.cancelRequest()
    })

    override fun showLoading() {
        fragment.show(supportFragmentManager, null)
    }

    override fun hideLoading(toFinish : Boolean) {
        fragment.dismiss()
        if (toFinish) finish()

    }

    override fun startMainActivity() {
        startActivity(Intent(applicationContext,MainActivity::class.java))
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
