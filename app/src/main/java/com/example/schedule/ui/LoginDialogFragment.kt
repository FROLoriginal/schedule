package com.example.schedule.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.schedule.R

class LoginDialogFragment internal constructor() : DialogFragment() {

    var actionAfterCancellation: () -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        builder.setView(inflater.inflate(R.layout.fragment_login, null))
        return builder.create()
    }

    override fun onCancel(dialog: DialogInterface) {
        actionAfterCancellation()
        super.onCancel(dialog)
    }
}
