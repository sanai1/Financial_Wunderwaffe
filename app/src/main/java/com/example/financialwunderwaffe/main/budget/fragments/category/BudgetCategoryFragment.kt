package com.example.financialwunderwaffe.main.budget.fragments.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.financialwunderwaffe.LoadingFragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.budget.BudgetFragment

class BudgetCategoryFragment : Fragment() {
    lateinit var budgetFragment: BudgetFragment
    lateinit var budgetCategoryMainFragment: BudgetCategoryMainFragment
    lateinit var budgetCategoryAddFragment: BudgetCategoryAddFragment
    lateinit var loadingFragment: LoadingFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_budget_category, container, false)

        budgetFragment = (parentFragment as BudgetFragment)
        budgetCategoryMainFragment = BudgetCategoryMainFragment()
        budgetCategoryAddFragment = BudgetCategoryAddFragment()
        loadingFragment = LoadingFragment()

        goToFragment(budgetCategoryMainFragment)

        return view
    }

    fun goToFragment(fragment: Fragment) = childFragmentManager.beginTransaction().also {
        it.replace(R.id.container_category, fragment)
        it.addToBackStack(null)
        it.commit()
    }

}