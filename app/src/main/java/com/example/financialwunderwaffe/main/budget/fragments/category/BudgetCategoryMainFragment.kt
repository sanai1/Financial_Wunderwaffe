package com.example.financialwunderwaffe.main.budget.fragments.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.budget.BudgetViewModel
import com.example.financialwunderwaffe.retrofit.database.category.Category

class BudgetCategoryMainFragment : Fragment() {
    private lateinit var viewModel: BudgetViewModel
    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_budget_category_main, container, false)
        viewModel = (activity as MainActivity).budgetViewModel
        view.findViewById<RecyclerView>(R.id.list_category).visibility = View.GONE
        view.findViewById<ProgressBar>(R.id.progressBarCategoryMain).visibility = View.VISIBLE

        view.findViewById<ImageView>(R.id.imageViewCategoryAdd).setOnClickListener {
            (parentFragment as BudgetCategoryFragment).apply {
                goToFragment(budgetCategoryAddFragment)
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) { categoryList ->
            initRecyclerViewCategory(categoryList.sortedBy { it.name })
        }

        return view
    }

    private fun initRecyclerViewCategory(listCategory: List<Category>) {
        view.findViewById<ProgressBar>(R.id.progressBarCategoryMain).visibility = View.GONE
        view.findViewById<RecyclerView>(R.id.list_category).apply {
            visibility = View.VISIBLE
            adapter = CategoryAdapter(
                listCategory.map {
                    CategoryState(
                        id = it.id,
                        title = it.name,
                        type = it.type
                    )
                }
            ) { categoryState ->
                (activity as MainActivity).toast("Нажали на ${categoryState.title} категорию")
            }
            layoutManager = LinearLayoutManager(activity)
        }
    }

}