package com.example.financialwunderwaffe.main.briefcase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.databinding.FragmentMainBriefcaseBinding
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.retrofit.database.asset.Asset

class BriefcaseFragment : Fragment() {
    private var _binding: FragmentMainBriefcaseBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BriefcaseViewModel
    private lateinit var briefcaseDialogAssetAdd: BriefcaseDialogAssetAdd

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBriefcaseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = (activity as MainActivity).briefcaseViewModel

        briefcaseDialogAssetAdd =
            BriefcaseDialogAssetAdd((activity as MainActivity).printToast) { title ->
                viewModel.createAsset(
                    token = (activity as MainActivity).basicLoginAndPassword,
                    uid = (activity as MainActivity).uid,
                    asset = Asset(
                        userUID = (activity as MainActivity).uid,
                        title = title
                    )
                )
        }
        binding.imageViewAssetAdd.setOnClickListener {
            val listAssetString = mutableListOf(
                "Фондовый рынок",
                "Недвижимость",
                "Криптовалюта",
                "Бизнес",
                "Свой вариант"
            ).filter { s ->
                if (viewModel.listAssets.value == null || s == "Свой вариант") true
                else (s in viewModel.listAssets.value!!.map { it.title }).not()
            }
            briefcaseDialogAssetAdd.setAssetList(listAssetString)
            briefcaseDialogAssetAdd.show(childFragmentManager, "DIALOG_ASSET_ADD")
        }
        viewModel.listAssets.observe(viewLifecycleOwner) {
            initAllInfo(it)
            initListAssets(it)
        }
        viewModel.updateListAssets(
            (activity as MainActivity).basicLoginAndPassword, (activity as MainActivity).uid
        )

        return root
    }

    fun closeAssetInfoFragment() {
        binding.containerBriefcase.visibility = View.GONE
        binding.imageViewAssetAdd.visibility = View.VISIBLE
        binding.listAsset.visibility = View.VISIBLE
        viewModel.clearListInformation()
    }

    private fun initAllInfo(listAssets: List<Asset>) {
        binding.textViewCountAsset.text = "Кол-во активов: ${listAssets.size}"
        binding.textViewAmountAsset.text =
            listAssets.sumOf { it.amount }.toString().reversed().chunked(3).joinToString(" ")
                .reversed() + "₽"
    }

    private fun initListAssets(listAssets: List<Asset>) =
        binding.listAsset.apply {
            adapter = AssetAdapter(
                listAssets.map {
                    AssetState(
                        id = it.id,
                        title = it.title,
                        amount = it.amount
                    )
                }
            ) { assetState ->
                if (assetState.title == "Фиат") {
                    (activity as MainActivity).toast("Фиат считается автоматически по вашим транзакциям")
                } else {
                    viewModel.selectAsset(assetState)
                    binding.containerBriefcase.visibility = View.VISIBLE
                    binding.imageViewAssetAdd.visibility = View.GONE
                    binding.listAsset.visibility = View.GONE
                    childFragmentManager.beginTransaction().apply {
                        replace(R.id.container_briefcase, BriefcaseAssetInfoFragment())
                        addToBackStack(null)
                        commit()
                    }
                }
            }
            layoutManager = LinearLayoutManager(requireContext())
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}