package com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity

class BriefcaseSharesOfAssetsMainFragment : Fragment() {

    private lateinit var shareOfAssetsAdapter: ShareOfAssetsAdapter
    private lateinit var recyclerViewSharesOfAssets: RecyclerView
    private lateinit var layoutManagerShareOfAssets: RecyclerView.LayoutManager
    var listStateSharesOfAssetsState: List<SharesOfAssetsState> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_briefcase_shares_of_assets_main, container, false)

        recyclerViewSharesOfAssets = view.findViewById(R.id.list_shares_of_assets)
        layoutManagerShareOfAssets = LinearLayoutManager((activity as MainActivity).context)

        initRecycleViewSharesOfAssets()

        return view
    }

    private fun initRecycleViewSharesOfAssets() {
        val onStateClickShareOfAssets = object : ShareOfAssetsAdapter.OnStateClickShareOfAssets {
            override fun onStateClickShareOfAssets(sharesOfAssetsState: SharesOfAssetsState, position: Int) {
                (activity as MainActivity).toast("Нажали на Item - ${sharesOfAssetsState.typeAssets}")
            }
        }
        shareOfAssetsAdapter = ShareOfAssetsAdapter(listStateSharesOfAssetsState, onStateClickShareOfAssets)
        recyclerViewSharesOfAssets.adapter = shareOfAssetsAdapter
        recyclerViewSharesOfAssets.layoutManager = layoutManagerShareOfAssets
    }

}