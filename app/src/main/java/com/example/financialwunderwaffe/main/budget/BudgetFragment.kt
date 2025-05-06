package com.example.financialwunderwaffe.main.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.databinding.FragmentMainBudgetBinding
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.budget.fragments.BudgetTransactionFragment
import com.example.financialwunderwaffe.main.budget.fragments.category.BudgetCategoryFragment
import com.example.financialwunderwaffe.main.budget.fragments.history.BudgetHistoryFragment

class BudgetFragment : Fragment() {
    private var _binding: FragmentMainBudgetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBudgetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textViewTitleBudget.text = getString(R.string.transaction)

        binding.imageViewMenuBudget.setOnClickListener{
            binding.drawerBudget.openDrawer(GravityCompat.START)
        }

        binding.navigationBudget.setNavigationItemSelectedListener {menuitem ->
            when (menuitem.itemId) {
                R.id.nav_budget_history -> {
                    binding.textViewTitleBudget.text = getString(R.string.history)
                    go_to_fragment(BudgetHistoryFragment())
                }
                R.id.nav_budget_transaction -> {
                    binding.textViewTitleBudget.text = getString(R.string.transaction)
                    go_to_fragment(BudgetTransactionFragment())
                }
                R.id.nav_budget_report -> {
                    (activity as MainActivity).toast("Этот раздел перенесен в 'Аналитику' в главном меню")
//                    binding.textViewTitleBudget.text = getString(R.string.report)
//                    go_to_fragment(BudgetReportFragment())
                }
                R.id.nav_budget_category -> {
                    binding.textViewTitleBudget.text = getString(R.string.category)
                    go_to_fragment(BudgetCategoryFragment())
                }
            }
            binding.drawerBudget.closeDrawer(GravityCompat.START)
            true
        }
        binding.navigationBudget.itemIconTintList = null

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