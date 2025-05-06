package com.example.financialwunderwaffe.main.budget.fragments.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.budget.BudgetViewModel
import com.example.financialwunderwaffe.retrofit.database.category.Category

class BudgetCategoryAddFragment : Fragment() {
    private lateinit var view: View
    private lateinit var viewModel: BudgetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_budget_category_add, container, false)
        viewModel = (activity as MainActivity).budgetViewModel

        view.findViewById<ImageView>(R.id.imageViewBackCategoryMain).setOnClickListener {
            (parentFragment as BudgetCategoryFragment).apply {
                goToFragment(budgetCategoryMainFragment)
            }
        }

        view.findViewById<Button>(R.id.buttonCategoryAdd).setOnClickListener {
            val name = view.findViewById<EditText>(R.id.editTextTextCategoryName).text.toString()
            if (name.isEmpty()) {
                (activity as MainActivity).toast("Введите название категории")
                return@setOnClickListener
            }
            val type =
                when (view.findViewById<RadioGroup>(R.id.radioGroupCategoryType).checkedRadioButtonId) {
                    view.findViewById<RadioButton>(R.id.radioButtonIncome).id -> true
                    view.findViewById<RadioButton>(R.id.radioButtonExpense).id -> false
                    else -> {
                        (activity as MainActivity).toast("Выберите тип категории")
                        return@setOnClickListener
                    }
                }
            viewModel.createCategory(
                (activity as MainActivity).basicLoginAndPassword, Category(
                    name = name,
                    type = type,
                    userUID = (activity as MainActivity).uid.toString()
                )
            )
            (parentFragment as BudgetCategoryFragment).apply {
                goToFragment(budgetCategoryMainFragment)
            }
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        view.findViewById<EditText>(R.id.editTextTextCategoryName).setText("")
        view.findViewById<RadioGroup>(R.id.radioGroupCategoryType).clearCheck()
    }

}