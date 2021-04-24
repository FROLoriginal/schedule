package com.example.schedule.ui.login

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
import androidx.appcompat.app.AppCompatActivity
import com.example.schedule.R
import com.example.schedule.Util.toast
import com.example.schedule.ui.MainActivity

class LoginActivity : AppCompatActivity(), LoginLoadingView {

    private lateinit var groupInput: EditText
    private lateinit var button: Button
    private lateinit var presenter: LoginActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        groupInput = findViewById(R.id.group)
        button = findViewById(R.id.getScheduleButton)
        button.isEnabled = false
        groupInput.addTextChangedListener(textChangedListener)
        groupInput.setOnEditorActionListener(inputGroup)
        presenter = LoginActivityPresenter(this, applicationContext)
    }

    fun onClick(v: View?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(groupInput.windowToken, 0)
        showLoading()
        val editText = groupInput.text.toString()
        Thread { presenter.makeRequest(editText) }.start()
    }

    override fun showGroupIfNotExist() {
        toast(R.string.unknownGroup)
        groupInput.setText("")
    }

    override fun showConnectionError() {
        toast(R.string.connectionError)
    }

    override fun showInternalError() {
        toast(R.string.internalErrorOccurred)
    }

    private val fragment = LoginDialogFragment()
            .also { it.actionAfterCancellation = { presenter.cancelRequest() } }


    override fun showLoading() {
        fragment.show(supportFragmentManager, null)
    }

    override fun hideLoading(isLoadingError: Boolean) {
        fragment.dismiss()
        if (!isLoadingError) finish()

    }

    override fun startMainActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }

    private val textChangedListener = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            button.isEnabled = s.toString() != ""
        }
    }

    private val inputGroup = OnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
        if (actionId == EditorInfo.IME_ACTION_DONE && button.isEnabled) {
            onClick(null)
            button.isEnabled = false
            true
        } else false
    }
}
