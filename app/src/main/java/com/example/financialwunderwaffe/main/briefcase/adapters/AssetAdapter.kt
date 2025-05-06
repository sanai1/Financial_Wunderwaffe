package com.example.financialwunderwaffe.main.briefcase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.briefcase.states.AssetState

class AssetAdapter(
    private val listAssetState: List<AssetState>,
    private val callback: (AssetState) -> Unit
) : RecyclerView.Adapter<AssetAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_asset_briefcase, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listAssetState.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(listAssetState[position])
        holder.click(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val linearLayoutAssetItem: LinearLayout =
            view.findViewById(R.id.linearLayoutAssetItem)
        private val textViewAssetTitle: TextView = view.findViewById(R.id.textViewAssetTitle)
        private val textViewAssetAmount: TextView = view.findViewById(R.id.textViewAssetAmount)

        fun click(position: Int) = linearLayoutAssetItem.setOnClickListener {
            callback(listAssetState[position])
        }

        fun update(assetState: AssetState) {
            textViewAssetTitle.text = assetState.title
            textViewAssetAmount.text =
                assetState.amount.toString().reversed().chunked(3).joinToString(" ").reversed()
        }
    }
}