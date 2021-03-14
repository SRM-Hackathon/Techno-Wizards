package com.example.product
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class dataPickerFragment : DialogFragment() {
    var c = Calendar.getInstance()
    var year = c[Calendar.YEAR]
    var month = c[Calendar.MONTH]
    var day = c[Calendar.DAY_OF_MONTH]
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(requireActivity(), activity as OnDateSetListener?, year, month, day)
    }
}