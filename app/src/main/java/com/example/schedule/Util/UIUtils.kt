package com.example.schedule.Util

import android.content.Context
import android.view.View
import android.widget.Toast

object UIUtils {
    fun Context.toast(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}
