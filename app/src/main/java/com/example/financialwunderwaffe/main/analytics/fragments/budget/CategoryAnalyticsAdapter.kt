package com.example.financialwunderwaffe.main.analytics.fragments.budget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.retrofit.database.category.Category

class CategoryAnalyticsAdapter(
    private val listCategory: MutableList<Pair<Category, Boolean>>,
    private val callback: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAnalyticsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_category_report, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listCategory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(listCategory[position])
        holder.click(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewCategoryTitleReport: TextView =
            view.findViewById(R.id.textViewCategoryTitleReport)

        fun click(position: Int) {
            textViewCategoryTitleReport.setOnClickListener {
                listCategory[position] =
                    listCategory[position].copy(second = listCategory[position].second.not())
                textViewCategoryTitleReport.background = ContextCompat.getDrawable(
                    itemView.context,
                    when (listCategory[position].second) {
                        true -> if (listCategory[position].first.type) {
                            R.drawable.item_selected_income
                        } else {
                            R.drawable.item_selected_expense
                        }

                        false -> if (listCategory[position].first.type) {
                            R.drawable.item_normal_income
                        } else {
                            R.drawable.item_normal_expense
                        }
                    }
                )
                callback(listCategory[position].first)
            }
        }

        fun update(pair: Pair<Category, Boolean>) {
            textViewCategoryTitleReport.text = pair.first.name
            textViewCategoryTitleReport.background = ContextCompat.getDrawable(
                itemView.context,
                when (pair.first.type) {
                    true -> R.drawable.item_selected_income
                    false -> R.drawable.item_selected_expense
                }
            )
        }
    }
}