package com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.financialwunderwaffe.LoadingFragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments.main.BriefcaseSharesOfAssetsMainFragment
import com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments.BriefcaseSharesOfAssetsQuestionnaireNotFoundFragment
import com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments.main.SharesOfAssetsState

class BriefcaseSharesOfAssetsFragment : Fragment() {

    lateinit var briefcaseSharesOfAssetsQuestionnaireNotFound: BriefcaseSharesOfAssetsQuestionnaireNotFoundFragment
    lateinit var briefcaseSharesOfAssetsMainFragment: BriefcaseSharesOfAssetsMainFragment
    lateinit var loadingFragment: LoadingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_briefcase_shares_of_assets, container, false)

        briefcaseSharesOfAssetsQuestionnaireNotFound =
            BriefcaseSharesOfAssetsQuestionnaireNotFoundFragment()
        briefcaseSharesOfAssetsMainFragment =
            BriefcaseSharesOfAssetsMainFragment()
        loadingFragment = LoadingFragment()

        goToFragment(loadingFragment)
        requestGetSharesOfAssets()

        return view
    }

    fun goToFragment(fragment: Fragment) {
        val briefcaseSharesOfAssets = childFragmentManager.beginTransaction()
        briefcaseSharesOfAssets.replace(R.id.container_briefcase_shares_of_assets, fragment)
        briefcaseSharesOfAssets.addToBackStack(null)
        briefcaseSharesOfAssets.commit()
    }

    private fun requestGetSharesOfAssets() {
        // TODO: выполнить запрос для получения данных по распределению долей активов
        val fl = true
        if (fl) {
            // данные успешно получены
            briefcaseSharesOfAssetsMainFragment.listStateSharesOfAssetsState = listOf(
                SharesOfAssetsState(
                    typeAssets = "Акции",
                    now = 10000,
                    goal = 5000,
                    delta = -5000,
                    color = listOf(0,0,120)
                ),
                SharesOfAssetsState(
                    typeAssets = "Облигации",
                    now = 10000,
                    goal = 5000,
                    delta = -5000,
                    color = listOf(0,128,128)
                ),
                SharesOfAssetsState(
                    typeAssets = "ПИФ",
                    now = 10000,
                    goal = 5000,
                    delta = -5000,
                    color = listOf(128,128,0)
                ),
                SharesOfAssetsState(
                    typeAssets = "Недвижимость",
                    now = 10000,
                    goal = 5000,
                    delta = -5000,
                    color = listOf(128,0,0)
                ),
                SharesOfAssetsState(
                    typeAssets = "Криптовалюта",
                    now = 10000,
                    goal = 5000,
                    delta = -5000,
                    color = listOf(128,0,128)
                ),
                SharesOfAssetsState(
                    typeAssets = "Вклады и счета",
                    now = 10000,
                    goal = 5000,
                    delta = -5000
                )
            )
            goToFragment(briefcaseSharesOfAssetsMainFragment)
        } else {
            // необходимо пройти анкету
            goToFragment(briefcaseSharesOfAssetsQuestionnaireNotFound)
        }
    }



}