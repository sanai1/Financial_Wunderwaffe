package com.example.financialwunderwaffe.main.briefcase

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R

class AssetInformationAdapter(
    private val listAssetInformationState: List<AssetInformationState>,
    private val callback: (AssetInformationState) -> Unit
) : RecyclerView.Adapter<AssetInformationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_asset_information_briefcase, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listAssetInformationState.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(listAssetInformationState[position])
        holder.click(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val linearLayoutItem: LinearLayout =
            view.findViewById(R.id.linearLayoutAssetInformationItem)
        private val textViewTitle: TextView = view.findViewById(R.id.textViewAssetTitleInformation)
        private val textViewDate: TextView = view.findViewById(R.id.textViewDateAssetInformation)
        private val textViewAmount: TextView =
            view.findViewById(R.id.textViewAmountAssetInformation)
        private val textViewPercentage: TextView =
            view.findViewById(R.id.textViewPercentageAssetInformation)

        fun click(position: Int) = linearLayoutItem.setOnClickListener {
            callback(listAssetInformationState[position])
        }

        fun update(assetInformationState: AssetInformationState) {
            textViewDate.text = assetInformationState.date
            if (assetInformationState.amount == "0 -> 0") {
                textViewTitle.text = "Создание"
                textViewAmount.visibility = View.GONE
                return
            }
            textViewTitle.text = assetInformationState.title
            textViewAmount.text = assetInformationState.amount
            if (assetInformationState.percentage == null) {
                textViewAmount.setTextColor(
                    when (assetInformationState.amount[0]) {
                        '+' -> Color.rgb(0, 200, 0)
                        else -> Color.RED
                    }
                )
            } else {
                textViewPercentage.apply {
                    text = assetInformationState.percentage
                    visibility = View.VISIBLE
                    setTextColor(
                        when (assetInformationState.percentage[1]) {
                            '-' -> Color.RED
                            else -> Color.rgb(0, 200, 0)
                        }
                    )
                }
            }
        }
    }
}