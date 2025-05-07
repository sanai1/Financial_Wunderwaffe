package com.example.financialwunderwaffe.main.budget.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.budget.BudgetViewModel
import com.example.financialwunderwaffe.retrofit.database.category.Category
import com.example.financialwunderwaffe.retrofit.database.transaction.Transaction
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class BudgetTransactionFragment : Fragment() {
    private lateinit var spinner: Spinner
    private lateinit var viewModel: BudgetViewModel
    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_budget_transaction, container, false)
        viewModel = (activity as MainActivity).budgetViewModel
        spinner = view.findViewById(R.id.spinnerCategory)

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            initSpinner(categories.sortedBy { it.name })
        }
        viewModel.typeCategory.observe(viewLifecycleOwner) {
            if (viewModel.categories.value != null) initSpinner(viewModel.categories.value!!.sortedBy { it.name })
        }

        view.findViewById<RadioGroup>(R.id.radioGroupCategoryInTransaction).check(
            view.findViewById<RadioButton>(R.id.radioButtonExpenseInTransaction).id
        )
        viewModel.setTypeCategory(false)
        view.findViewById<RadioButton>(R.id.radioButtonExpenseInTransaction).setOnClickListener {
            viewModel.setTypeCategory(false)
        }
        view.findViewById<RadioButton>(R.id.radioButtonIncomeInTransaction).setOnClickListener {
            viewModel.setTypeCategory(true)
        }

        view.findViewById<ImageView>(R.id.imageViewDateTransaction).setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .setSelection(Calendar.getInstance().timeInMillis)
                .build()
            datePicker.show(childFragmentManager, "DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                val selDate =
                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selectedDate)
                if (LocalDate.parse(selDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        .isAfter(LocalDate.now())
                ) {
                    (activity as MainActivity).toast("Нельзя выбрать будущую дату")
                } else {
                    setDate(selDate)
                }
            }
        }
        setDate(LocalDate.now().toString().split("-").reversed().joinToString("."))

        view.findViewById<Button>(R.id.buttonTransactionAdd).setOnClickListener {
            val category =
                when (view.findViewById<RadioGroup>(R.id.radioGroupCategoryInTransaction).checkedRadioButtonId) {
                    view.findViewById<RadioButton>(R.id.radioButtonExpenseInTransaction).id -> {
                        viewModel.categories.value?.first { it.name == spinner.selectedItem as String && !it.type }
                    }

                    else -> {
                        viewModel.categories.value?.first { it.name == spinner.selectedItem as String && it.type }
                    }
                }
            if (category == null) {
                (activity as MainActivity).toast("Внутренняя ошибка: категория не существует")
                return@setOnClickListener
            }
            val amount =
                view.findViewById<EditText>(R.id.editTextNumberDecimalAmountTransaction).text.toString()
            if (amount.isEmpty()) {
                (activity as MainActivity).toast("Введите сумму транзакции")
                return@setOnClickListener
            }
            val date = view.findViewById<TextView>(R.id.textViewDateTransactionAdd).text.toString()
            if (date.isEmpty()) {
                (activity as MainActivity).toast("Введите дату транзакции")
                return@setOnClickListener
            }
            viewModel.createTransaction(
                (activity as MainActivity).basicLoginAndPassword, Transaction(
                    categoryID = category.id,
                    amount = amount.toLong(),
                    date = date,
                    type = view.findViewById<CheckBox>(R.id.checkBoxTransactionType).isChecked,
                    description = view.findViewById<EditText>(R.id.editTextTextMultiLineDescriptionTransaction).text.toString(),
                    userUID = (activity as MainActivity).uid
                )
            )
            goToDefault()
        }

        return view
    }

    private fun initSpinner(listCategory: List<Category>) {
        val adapterSpinner = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listCategory.filter {
                (it.name in listOf(
                    "Покупка актива",
                    "Продажа актива"
                )).not() && it.type == viewModel.typeCategory.value
            }.map { it.name }
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner
        setBlackColor()
    }

    private fun setBlackColor() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                (view as? TextView)?.apply {
                    gravity = Gravity.CENTER
                    setTextColor(Color.BLACK)
                    textSize = 25f
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setDate(dataString: String) {
        view.findViewById<TextView>(R.id.textViewDateTransactionAdd).text = dataString
    }

    private fun goToDefault() {
        view.findViewById<EditText>(R.id.editTextNumberDecimalAmountTransaction).setText("")
        view.findViewById<CheckBox>(R.id.checkBoxTransactionType).isChecked = false
        view.findViewById<EditText>(R.id.editTextTextMultiLineDescriptionTransaction).setText("")
    }

}