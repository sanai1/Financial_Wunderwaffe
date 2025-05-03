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
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class BriefcaseDialogAssetTransactionAdd(
    private val toast: (String) -> Unit,
    private val callback: (String, Long, Boolean) -> Unit
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
        val view = inflater.inflate(R.layout.dialog_fragment_briefcase_asset_transaction_add, null)

        view.findViewById<TextView>(R.id.textViewAssetTitleTransactionAdd).text = "Актив: $title"
        val textViewDate = view.findViewById<TextView>(R.id.textViewDateAssetTransaction)
        textViewDate.text = LocalDate.now().toString().split("-").reversed().joinToString(".")

        view.findViewById<ImageView>(R.id.imageViewDateAssetTransaction).setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .setSelection(Calendar.getInstance().timeInMillis)
                .build()
            datePicker.show(childFragmentManager, "DATE_PICKER_ASSET_TRANSACTION")
            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                textViewDate.text =
                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selectedDate)
            }
        }

        view.findViewById<Button>(R.id.buttonAssetTransactionAdd).setOnClickListener {
            val date = textViewDate.text.toString()
            val amount =
                view.findViewById<EditText>(R.id.editTextNumberDecimalAmountAssetTransaction).text.toString()
            if (amount.isEmpty()) {
                toast("Заполните сумму операции")
                return@setOnClickListener
            }
            val type =
                when (view.findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupAssetTransaction).checkedButtonId) {
                    view.findViewById<MaterialButton>(R.id.btn_buy).id -> false
                    else -> true
                }
            dismiss()
            callback(date, amount.toLong(), type)
        }

        return view
    }

}