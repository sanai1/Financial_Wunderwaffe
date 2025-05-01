package com.example.financialwunderwaffe.main.budget.fragments.category

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R

class CategoryAdapter(
    private val listCategoryState: List<CategoryState>,
    private val callback: (CategoryState) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_category_budget, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listCategoryState.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(listCategoryState[position])
        holder.click(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val linearLayoutColorCategory: LinearLayout =
            view.findViewById(R.id.linearLayoutColorCategory)
        private val textViewCategoryTitle: TextView = view.findViewById(R.id.textViewCategoryTitle)
        private val imageViewCategoryInfo: ImageView = view.findViewById(R.id.imageViewCategoryInfo)

        fun click(position: Int) = imageViewCategoryInfo.setOnClickListener {
            callback(listCategoryState[position])
        }

        fun update(categoryState: CategoryState) {
            val colorList = if (categoryState.type) {
                listOf(0, 128, 0)
            } else {
                listOf(128, 0, 0)
            }
            (linearLayoutColorCategory.background as GradientDrawable).setColor(
                Color.rgb(
                    colorList[0], colorList[1], colorList[2]
                )
            )
            textViewCategoryTitle.text = categoryState.title
        }

    }
}
