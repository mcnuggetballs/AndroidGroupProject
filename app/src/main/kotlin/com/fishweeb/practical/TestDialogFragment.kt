package com.fishweeb.practical

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle

class TestDialogFragment : DialogFragment() {
    override fun onCreateDialog(saveInstanceState: Bundle): Dialog {
        IsShown = true
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Confirm Pause?")
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->
                IsShown =
                    false
            }
            .setNegativeButton(
                "No"
            ) { dialog, id ->
                IsShown =
                    false
            }
        return builder.create()
    }

    override fun onCancel(dialog: DialogInterface) {
        IsShown = false
    }

    override fun onDismiss(dialog: DialogInterface) {
        IsShown = false
    }

    companion object {
        var IsShown: Boolean = false
    }
}
