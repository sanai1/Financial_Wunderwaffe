package com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments.BriefcaseSharesOfAssetsMainFragment
import com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments.BriefcaseSharesOfAssetsQuestionnaireNotFoundFragment

class BriefcaseSharesOfAssetsFragment : Fragment() {

    lateinit var briefcaseSharesOfAssetsQuestionnaireNotFound: BriefcaseSharesOfAssetsQuestionnaireNotFoundFragment
    lateinit var briefcaseSharesOfAssetsMainFragment: BriefcaseSharesOfAssetsMainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_briefcase_shares_of_assets, container, false)

        briefcaseSharesOfAssetsQuestionnaireNotFound =
            BriefcaseSharesOfAssetsQuestionnaireNotFoundFragment()
        briefcaseSharesOfAssetsMainFragment =
            BriefcaseSharesOfAssetsMainFragment()


        goToFragment(briefcaseSharesOfAssetsQuestionnaireNotFound)

        return view
    }

    fun goToFragment(fragment: Fragment) {
        val briefcaseSharesOfAssets = childFragmentManager.beginTransaction()
        briefcaseSharesOfAssets.replace(R.id.container_briefcase_shares_of_assets, fragment)
        briefcaseSharesOfAssets.addToBackStack(null)
        briefcaseSharesOfAssets.commit()
    }

}