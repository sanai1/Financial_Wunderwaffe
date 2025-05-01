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
import com.example.financialwunderwaffe.retrofit.database.category.Category
import com.example.financialwunderwaffe.retrofit.database.category.CategoryApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BudgetCategoryAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget_category_add, container, false)

        view.findViewById<ImageView>(R.id.imageViewBackCategoryMain).setOnClickListener {
            (parentFragment as BudgetCategoryFragment).goToFragment(
                (parentFragment as BudgetCategoryFragment).budgetCategoryMainFragment
            )
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
            addCategory(
                Category(
                    name = name,
                    type = type,
                    userUID = (activity as MainActivity).uid.toString()
                )
            )
        }

        return view
    }

    private fun addCategory(category: Category) {
        (parentFragment as BudgetCategoryFragment).goToFragment(
            (parentFragment as BudgetCategoryFragment).loadingFragment
        )
        CategoryApiClient.categoryAPIService.createCategory(
            token = (activity as MainActivity).basicLoginAndPassword,
            category = category
        ).enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    (parentFragment as BudgetCategoryFragment).goToFragment(
                        (parentFragment as BudgetCategoryFragment).budgetCategoryMainFragment
                    )
                } else {
                    (activity as MainActivity).toast("Ошибка сервера: ${response.code()}-${response.message()}")
                    (parentFragment as BudgetCategoryFragment).goToFragment(
                        (parentFragment as BudgetCategoryFragment).budgetCategoryAddFragment
                    )
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                (activity as MainActivity).toast("Ошибка сети: ${t.message}")
                (parentFragment as BudgetCategoryFragment).goToFragment(
                    (parentFragment as BudgetCategoryFragment).budgetCategoryAddFragment
                )
            }
        })
    }

}