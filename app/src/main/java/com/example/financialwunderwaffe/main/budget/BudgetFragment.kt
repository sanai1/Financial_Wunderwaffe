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
import com.example.financialwunderwaffe.main.budget.fragments.report.BudgetReportFragment
import com.example.financialwunderwaffe.retrofit.database.category.Category
import com.example.financialwunderwaffe.retrofit.database.category.CategoryApiClient
import com.example.financialwunderwaffe.retrofit.database.transaction.Transaction
import com.example.financialwunderwaffe.retrofit.database.transaction.TransactionApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BudgetFragment : Fragment() {
    private var _binding: FragmentMainBudgetBinding? = null
    private val binding get() = _binding!!
    val listCategory = mutableListOf<Category>()
    val listTransaction = mutableListOf<Transaction>()

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
                    binding.textViewTitleBudget.text = getString(R.string.report)
                    go_to_fragment(BudgetReportFragment())
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

        initCategory()
        go_to_fragment(BudgetTransactionFragment())

        return root
    }

    private fun go_to_fragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.container_budget, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun initCategory() = CoroutineScope(Dispatchers.IO).launch {
        try {
            (activity as MainActivity).basicLoginAndPassword
        } catch (e: Exception) {
            delay(250)
        }
        val response = CategoryApiClient.categoryAPIService.getByUserUID(
            token = (activity as MainActivity).basicLoginAndPassword,
            userUID = (activity as MainActivity).uid
        ).execute()
        if (response.isSuccessful && response.body() != null) {
            listCategory.clear()
            response.body()!!.forEach {
                listCategory.add(it)
            }
            initTransaction()
        } else {
            (activity as MainActivity).toast("Ошибка сервера или сети: ${response.code()}-${response.message()}")
        }
    }

    fun initTransaction() = CoroutineScope(Dispatchers.IO).launch {
        val response = TransactionApiClient.transactionAPIService.getBuUserUID(
            token = (activity as MainActivity).basicLoginAndPassword,
            userUID = (activity as MainActivity).uid
        ).execute()
        if (response.isSuccessful && response.body() != null) {
            listTransaction.clear()
            response.body()!!.forEach {
                listTransaction.add(it)
            }
        } else {
            (activity as MainActivity).toast("Ошибка сервера: ${response.code()}-${response.message()}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}