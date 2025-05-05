package com.example.financialwunderwaffe.main.analytics.fragments.asset

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.retrofit.database.asset.Asset

class AssetAnalyticsAdapter(
    private val listAsset: MutableList<Pair<Asset, Boolean>>,
    private val colorToAsset: Map<String, Int>,
    private val callback: (Asset) -> Unit
) : RecyclerView.Adapter<AssetAnalyticsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_text_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listAsset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(listAsset[position])
        holder.click(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewTitle: TextView = view.findViewById(R.id.textViewTitleItem)

        fun click(position: Int) = textViewTitle.setOnClickListener {
            listAsset[position] =
                listAsset[position].copy(second = listAsset[position].second.not())
            textViewTitle.background = when (listAsset[position].second) {
                true -> getBackground(listAsset[position].first.title)
                false -> getBackgroundDefault()
            }
            callback(listAsset[position].first)
        }

        fun update(pair: Pair<Asset, Boolean>) = textViewTitle.apply {
            text = pair.first.title
            background = getBackground(pair.first.title)
        }

        private fun getBackground(assetName: String): GradientDrawable {
            val back = getGradientDrawable()
            back.setColor(colorToAsset.getValue(assetName))
            return back
        }

        private fun getBackgroundDefault(): GradientDrawable {
            val back = getGradientDrawable()
            back.setColor(ContextCompat.getColor(itemView.context, R.color.light_gray))
            return back
        }

        private fun getGradientDrawable() = GradientDrawable().apply {
            val coef = itemView.context.resources.displayMetrics.density
            cornerRadius = 10 * coef
            setStroke((2 * coef).toInt(), Color.BLACK)
        }
    }
}