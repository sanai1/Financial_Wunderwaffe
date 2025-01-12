package com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments.main

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R

class ShareOfAssetsAdapter(
    private val listSharesOfAssetsState: List<SharesOfAssetsState>,
    private val onStateClickShareOfAssets: OnStateClickShareOfAssets
): RecyclerView.Adapter<ShareOfAssetsAdapter.ViewHolder>() {

    interface OnStateClickShareOfAssets {
        fun onStateClickShareOfAssets(sharesOfAssetsState: SharesOfAssetsState, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_share_of_assets_main, parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listSharesOfAssetsState.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(listSharesOfAssetsState[position])
        holder.click(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val linearLayout: LinearLayout = view.findViewById(R.id.linearLayoutAssetDetails)
        private val linearLayoutColorTypeAsset: LinearLayout = view.findViewById(R.id.linearLayoutColorTypeAsset)
        private val textViewTypeAssets: TextView = view.findViewById(R.id.textViewTypeAsset)
        private val textViewNow: TextView = view.findViewById(R.id.textViewShareOfAssetNow)
        private val textViewGoal: TextView = view.findViewById(R.id.textViewShareOfAssetGoal)
        private val textViewDelta: TextView = view.findViewById(R.id.textViewShareOfAssetDelta)

        fun click(position: Int) =
            linearLayout.setOnClickListener {
                onStateClickShareOfAssets.onStateClickShareOfAssets(listSharesOfAssetsState[position], position)
            }

        fun update(sharesOfAssetsState: SharesOfAssetsState) {
            (linearLayoutColorTypeAsset.background as GradientDrawable).setColor(Color.rgb(
                sharesOfAssetsState.color[0],
                sharesOfAssetsState.color[1],
                sharesOfAssetsState.color[2]
            ))
            textViewTypeAssets.text = sharesOfAssetsState.typeAssets
            (sharesOfAssetsState.now / 100L).toString().also { textViewNow.text = it }
            (sharesOfAssetsState.goal / 100L).toString().also { textViewGoal.text = it }
            (sharesOfAssetsState.delta / 100L).toString().also { textViewDelta.text = it }
        }

    }

}