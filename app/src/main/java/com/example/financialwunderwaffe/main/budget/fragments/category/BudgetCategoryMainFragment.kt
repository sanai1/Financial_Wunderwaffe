package com.example.financialwunderwaffe.main.budget.fragments.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity

class BudgetCategoryMainFragment : Fragment() {
    private lateinit var categoryStateAdapter: CategoryAdapter
    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var layoutManagerCategory: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget_category_main, container, false)

        view.findViewById<ImageView>(R.id.imageViewCategoryAdd).setOnClickListener {
            (parentFragment as BudgetCategoryFragment).goToFragment(
                (parentFragment as BudgetCategoryFragment).budgetCategoryAddFragment
            )
        }

        recyclerViewCategory = view.findViewById(R.id.list_category)
        layoutManagerCategory = LinearLayoutManager(activity)
        initRecyclerViewCategory()

        return view
    }

    private fun initRecyclerViewCategory() {
        categoryStateAdapter = CategoryAdapter(
            (parentFragment as BudgetCategoryFragment).budgetFragment.listCategory.map {
                CategoryState(
                    id = it.id,
                    title = it.name,
                    type = it.type
                )
            }
        ) { categoryState ->
            (activity as MainActivity).toast("Нажали на ${categoryState.title} категорию")
        }
        recyclerViewCategory.adapter = categoryStateAdapter
        recyclerViewCategory.layoutManager = layoutManagerCategory
    }

}