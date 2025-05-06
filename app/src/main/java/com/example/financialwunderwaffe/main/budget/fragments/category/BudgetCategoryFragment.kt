package com.example.financialwunderwaffe.main.budget.fragments.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.financialwunderwaffe.LoadingFragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BudgetCategoryFragment : Fragment() {
    lateinit var budgetCategoryMainFragment: BudgetCategoryMainFragment
    lateinit var budgetCategoryAddFragment: BudgetCategoryAddFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_budget_category, container, false)

        budgetCategoryMainFragment = BudgetCategoryMainFragment()
        budgetCategoryAddFragment = BudgetCategoryAddFragment()

        goToFragment(LoadingFragment())
        check()

        return view
    }

    private fun check() = CoroutineScope(Dispatchers.IO).launch {
        while ((activity as MainActivity).budgetViewModel.categories.value == null) {
            delay(50)
        }
        withContext(Dispatchers.Main) {
            goToFragment(budgetCategoryMainFragment)
        }
    }

    fun goToFragment(fragment: Fragment) = childFragmentManager.beginTransaction().also {
        it.replace(R.id.container_category, fragment)
        it.addToBackStack(null)
        it.commit()
    }

}