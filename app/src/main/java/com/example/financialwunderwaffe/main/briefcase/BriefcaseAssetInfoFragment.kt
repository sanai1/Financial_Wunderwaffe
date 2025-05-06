package com.example.financialwunderwaffe.main.briefcase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.briefcase.adapters.AssetInformationAdapter
import com.example.financialwunderwaffe.main.briefcase.dialogs.BriefcaseDialogAssetPriceAdd
import com.example.financialwunderwaffe.main.briefcase.dialogs.BriefcaseDialogAssetTransactionAdd
import com.example.financialwunderwaffe.main.briefcase.states.AssetInformationState
import com.example.financialwunderwaffe.retrofit.database.asset.information.model.AssetInformation
import com.example.financialwunderwaffe.retrofit.database.asset.information.model.AssetPrice
import com.example.financialwunderwaffe.retrofit.database.asset.information.model.AssetTransaction


class BriefcaseAssetInfoFragment : Fragment() {
    private lateinit var view: View
    private lateinit var viewModel: BriefcaseViewModel
    private lateinit var briefcaseDialogAssetTransactionAdd: BriefcaseDialogAssetTransactionAdd
    private lateinit var briefcaseDialogAssetPriceAdd: BriefcaseDialogAssetPriceAdd

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_briefcase_asset_info, container, false)

        viewModel = (activity as MainActivity).briefcaseViewModel
        briefcaseDialogAssetTransactionAdd = BriefcaseDialogAssetTransactionAdd(
            (activity as MainActivity).printToast
        ) { date, amount, type ->
            viewModel.createAssetTransaction(
                (activity as MainActivity).basicLoginAndPassword,
                (activity as MainActivity).uid,
                AssetTransaction(
                    assetID = viewModel.selectAsset.value?.id ?: 0,
                    isSale = type,
                    amount = amount,
                    date = date
                )
            )
        }
        briefcaseDialogAssetPriceAdd = BriefcaseDialogAssetPriceAdd(
            (activity as MainActivity).printToast
        ) { date, currentAmount ->
            viewModel.createAssetPrice(
                (activity as MainActivity).basicLoginAndPassword,
                (activity as MainActivity).uid,
                AssetPrice(
                    assetID = viewModel.selectAsset.value?.id ?: 0,
                    date = date,
                    oldPrice = viewModel.selectAsset.value?.amount ?: 0,
                    currentPrice = currentAmount
                )
            )
        }

        view.findViewById<ImageView>(R.id.imageViewBackBriefcaseMain).setOnClickListener {
            (parentFragment as BriefcaseFragment).closeAssetInfoFragment()
        }
        view.findViewById<ImageView>(R.id.imageViewUpdateAssetAmount).setOnClickListener {
            briefcaseDialogAssetPriceAdd.setTitle(viewModel.selectAsset.value?.title ?: "")
            briefcaseDialogAssetPriceAdd.show(childFragmentManager, "DIALOG_ASSET_PRICE_ADD")
        }

        view.findViewById<ImageView>(R.id.imageViewAssetTransactionAdd).setOnClickListener {
            briefcaseDialogAssetTransactionAdd.setTitle(viewModel.selectAsset.value?.title ?: "")
            briefcaseDialogAssetTransactionAdd.show(
                childFragmentManager,
                "DIALOG_ASSET_TRANSACTION_ADD"
            )
        }

        viewModel.selectAsset.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.textViewAssetTitleInfo).text = it.title
            view.findViewById<TextView>(R.id.textViewAssetAmountInfo).text =
                it.amount.toString().reversed().chunked(3).joinToString(" ").reversed() + "₽"
        }
        viewModel.listAssetInformation.observe(viewLifecycleOwner) {
            initListInformation(it)
        }
        viewModel.updateListInformation((activity as MainActivity).basicLoginAndPassword)

        return view
    }

    private fun initListInformation(listInformation: List<AssetInformation>) =
        view.findViewById<RecyclerView>(R.id.listAssetInfo).apply {
            adapter = AssetInformationAdapter(
                listInformation.map {
                    AssetInformationState(
                        assetId = it.assetId,
                        title = when (it.typeInformation) {
                            "price" -> "Переоценка"
                            else -> if (it.isSale == true) "Продажа актива" else "Покупка актива"
                        },
                        date = it.date,
                        amount = when (it.typeInformation) {
                            "price" -> "${
                                it.oldPrice.toString().reversed().chunked(3).joinToString(" ")
                                    .reversed()
                            } -> ${
                                it.currentPrice.toString().reversed().chunked(3).joinToString(" ")
                                    .reversed()
                            }"

                            else -> (if (it.isSale!!) "-" else "+") + it.amount.toString()
                                .reversed().chunked(3).joinToString(" ").reversed()
                        },
                        percentage = when (it.typeInformation) {
                            "price" -> "(${
                                if (it.oldPrice!! > it.currentPrice!!) "" else "+"
                            }${
                                if (it.oldPrice == 0L || it.currentPrice == 0L) "100"
                                else (10000 * (it.currentPrice - it.oldPrice).toDouble() / it.oldPrice.toDouble()).toLong()
                                    .toDouble() / 100.0
                            }%)"

                            else -> null
                        }
                    )
                }
            ) { assetInformationState ->
                (activity as MainActivity).toast("Нажали на ${assetInformationState.title}")
            }
            layoutManager = LinearLayoutManager(requireContext())
        }

}