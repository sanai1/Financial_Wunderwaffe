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
import com.example.financialwunderwaffe.main.budget.BudgetFragment
import com.example.financialwunderwaffe.retrofit.database.transaction.Transaction
import com.example.financialwunderwaffe.retrofit.database.transaction.TransactionApiClient
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class BudgetTransactionFragment : Fragment() {
    private lateinit var spinner: Spinner
    private lateinit var view: View
    private val listCategoryExpense = mutableListOf<String>()
    private val listCategoryIncome = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_budget_transaction, container, false)

        spinner = view.findViewById(R.id.spinnerCategory)
        initListsForSpinner()
        view.findViewById<RadioGroup>(R.id.radioGroupCategoryInTransaction).check(
            view.findViewById<RadioButton>(R.id.radioButtonExpenseInTransaction).id
        )
        view.findViewById<RadioButton>(R.id.radioButtonExpenseInTransaction).setOnClickListener {
            setExpenseCategory()
        }
        view.findViewById<RadioButton>(R.id.radioButtonIncomeInTransaction).setOnClickListener {
            setIncomeCategory()
        }

        view.findViewById<ImageView>(R.id.imageViewDateTransaction).setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .setSelection(Calendar.getInstance().timeInMillis)
                .build()
            datePicker.show(childFragmentManager, "DATE_PICKER")
            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                setDate(SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selectedDate))
            }
        }
        setDate(LocalDate.now().toString().split("-").reversed().joinToString("."))

        view.findViewById<Button>(R.id.buttonTransactionAdd).setOnClickListener {
            val category =
                when (view.findViewById<RadioGroup>(R.id.radioGroupCategoryInTransaction).checkedRadioButtonId) {
                    view.findViewById<RadioButton>(R.id.radioButtonExpenseInTransaction).id -> {
                        (parentFragment as BudgetFragment).listCategory.first { it.name == spinner.selectedItem as String && !it.type }
                    }

                    else -> {
                        (parentFragment as BudgetFragment).listCategory.first { it.name == spinner.selectedItem as String && it.type }
                    }
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
            saveTransaction(
                Transaction(
                    categoryID = category.id,
                    amount = amount.toLong(),
                    date = date,
                    type = view.findViewById<CheckBox>(R.id.checkBoxTransactionType).isChecked,
                    description = view.findViewById<EditText>(R.id.editTextTextMultiLineDescriptionTransaction).text.toString(),
                    userUID = (activity as MainActivity).uid
                )
            )
        }

        return view
    }

    private fun initListsForSpinner() = CoroutineScope(Dispatchers.IO).launch {
        delay(500)
        (parentFragment as BudgetFragment).listCategory.forEach {
            if (it.type) {
                listCategoryIncome.add(it.name)
            } else {
                listCategoryExpense.add(it.name)
            }
        }
        withContext(Dispatchers.Main) {
            setExpenseCategory()
        }
    }

    private fun setExpenseCategory() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listCategoryExpense.filter { it != "Покупка актива" }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        setBlackColor()
    }

    private fun setIncomeCategory() {
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                listCategoryIncome.filter { it != "Продажа актива" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
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

    private fun saveTransaction(transaction: Transaction) {
        TransactionApiClient.transactionAPIService.createTransaction(
            token = (activity as MainActivity).basicLoginAndPassword,
            transaction = transaction
        ).enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    (activity as MainActivity).toast("Транзакция добавлена")
                    goToDefault()
                    (parentFragment as BudgetFragment).initTransaction()
                } else {
                    (activity as MainActivity).toast("Ошибка сервера: ${response.code()}-${response.message()}")
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                (activity as MainActivity).toast("Ошибка сети: ${t.message}")
            }
        })
    }

    private fun goToDefault() {
        view.findViewById<EditText>(R.id.editTextNumberDecimalAmountTransaction).setText("")
        view.findViewById<CheckBox>(R.id.checkBoxTransactionType).isChecked = false
        view.findViewById<EditText>(R.id.editTextTextMultiLineDescriptionTransaction).setText("")
    }

}