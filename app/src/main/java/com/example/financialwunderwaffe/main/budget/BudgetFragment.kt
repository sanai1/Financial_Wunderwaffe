package com.example.financialwunderwaffe.main.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.databinding.FragmentMainBudgetBinding
import com.example.financialwunderwaffe.main.budget.fragments.BudgetCategoryFragment
import com.example.financialwunderwaffe.main.budget.fragments.BudgetHistoryFragment
import com.example.financialwunderwaffe.main.budget.fragments.BudgetReportFragment
import com.example.financialwunderwaffe.main.budget.fragments.BudgetTransactionFragment

class BudgetFragment : Fragment() {

    private var _binding: FragmentMainBudgetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBudgetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textViewTitleBudget.setText(getString(R.string.title_budget))

        binding.imageViewMenuBudget.setOnClickListener{
            binding.drawerBudget.openDrawer(GravityCompat.START)
        }

        binding.navigationBudget.setNavigationItemSelectedListener {menuitem ->
            when (menuitem.itemId) {
                R.id.nav_budget_history -> go_to_fragment(BudgetHistoryFragment())
                R.id.nav_budget_transaction -> go_to_fragment(BudgetTransactionFragment())
                R.id.nav_budget_report -> go_to_fragment(BudgetReportFragment())
                R.id.nav_budget_category -> go_to_fragment(BudgetCategoryFragment())
            }
            binding.drawerBudget.closeDrawer(GravityCompat.START)
            true
        }

        go_to_fragment(BudgetTransactionFragment())

        return root
    }

    private fun go_to_fragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.container_budget, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}