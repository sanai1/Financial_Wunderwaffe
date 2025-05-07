package com.example.financialwunderwaffe.main.briefcase.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.financialwunderwaffe.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class BriefcaseDialogAssetTransactionAdd(
    private val toast: (String) -> Unit,
    private val callback: (String, Long, Boolean) -> Unit
) : DialogFragment() {
    private lateinit var title: String
    private var type = false
    private lateinit var view: View

    fun setTitle(newTitle: String) {
        title = newTitle
    }

    fun setType(newType: Boolean) {
        type = newType
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.dialog_fragment_briefcase_asset_transaction_add, null)

        view.apply {
            findViewById<TextView>(R.id.textViewAssetTitleTransactionAdd).text = "Актив: $title"
            findViewById<MaterialButton>(R.id.btnType).apply {
                text = when (type) {
                    true -> "Продажа"
                    false -> "Покупка"
                }
                setBackgroundColor(
                    when (type) {
                        true -> ContextCompat.getColor(requireContext(), R.color.red_selected)
                        false -> ContextCompat.getColor(requireContext(), R.color.green_selected)
                    }
                )
            }
        }
        val textViewDate = view.findViewById<TextView>(R.id.textViewDateAssetTransaction)
        textViewDate.text = LocalDate.now().toString().split("-").reversed().joinToString(".")

        view.findViewById<ImageView>(R.id.imageViewDateAssetTransaction).setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .setSelection(Calendar.getInstance().timeInMillis)
                .build()
            datePicker.show(childFragmentManager, "DATE_PICKER_ASSET_TRANSACTION")
            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                val selDate =
                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selectedDate)
                if (LocalDate.parse(selDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        .isAfter(LocalDate.now())
                ) {
                    toast("Нельзя выбрать будущую дату")
                } else {
                    textViewDate.text = selDate
                }
            }
        }

        view.findViewById<Button>(R.id.buttonAssetTransactionAdd).apply {
            text = when (type) {
                true -> "Продать"
                false -> "Купить"
            }
            setOnClickListener {
                val date = textViewDate.text.toString()
                val amount =
                    view.findViewById<EditText>(R.id.editTextNumberDecimalAmountAssetTransaction).text.toString()
                if (amount.isEmpty()) {
                    toast("Заполните сумму операции")
                    return@setOnClickListener
                }
                dismiss()
                callback(date, amount.toLong(), type)
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        view.findViewById<EditText>(R.id.editTextNumberDecimalAmountAssetTransaction).setText("")
    }

}