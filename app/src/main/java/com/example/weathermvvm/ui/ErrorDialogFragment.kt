package com.example.weathermvvm.ui

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import com.example.weathermvvm.R

/**
 * View to shows errors
 */

class ErrorDialogFragment : DialogFragment() {

    private var message: String = ""

    companion object {
        const val ERROR_FRAGMENT_DIALOG_TAG = "ERROR_FRAGMENT_DIALOG_TAG"
        private const val MESSAGE = "MESSAGE"

        fun newInstance(message: String): ErrorDialogFragment {
            val args = Bundle()
            args.putString(MESSAGE, message)
            val fragment = ErrorDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        message = if (savedInstanceState == null) {
            arguments.getString(MESSAGE)
        } else {
            savedInstanceState.getString(MESSAGE)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(MESSAGE, message)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
                .setTitle(getString(R.string.notification_fragment_error_title))
        return builder.create()
    }
}