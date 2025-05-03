package com.example.financialwunderwaffe.main.briefcase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.financialwunderwaffe.R
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class BriefcaseDialogAssetPriceAdd(
    private val toast: (String) -> Unit,
    private val callback: (String, Long) -> Unit
) : DialogFragment() {
    private lateinit var title: String

    fun setTitle(newTitle: String) {
        title = newTitle
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_briefcase_asset_price_add, null)

        view.findViewById<TextView>(R.id.textViewAssetTitlePriceAdd).text = title

        val textViewDate = view.findViewById<TextView>(R.id.textViewDateAssetPrice)
        textViewDate.text = LocalDate.now().toString().split("-").reversed().joinToString(".")

        view.findViewById<ImageView>(R.id.imageViewDateAssetPrice).setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .setSelection(Calendar.getInstance().timeInMillis)
                .build()
            datePicker.show(childFragmentManager, "DATE_PICKER_ASSET_PRICE")
            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                textViewDate.text =
                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selectedDate)
            }
        }

        view.findViewById<Button>(R.id.buttonAssetPriceAdd).setOnClickListener {
            val currentAmount =
                view.findViewById<EditText>(R.id.editTextNumberDecimalCurrentAmountAssetPrice).text.toString()
            if (currentAmount.isEmpty()) {
                toast("Введите новую стоимость актива")
                return@setOnClickListener
            }
            dismiss()
            callback(textViewDate.text.toString(), currentAmount.toLong())
        }

        return view
    }
}