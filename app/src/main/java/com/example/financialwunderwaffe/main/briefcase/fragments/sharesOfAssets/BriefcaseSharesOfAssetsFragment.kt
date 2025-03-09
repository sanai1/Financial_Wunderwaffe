package com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.financialwunderwaffe.LoadingFragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments.main.BriefcaseSharesOfAssetsMainFragment
import com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments.BriefcaseSharesOfAssetsQuestionnaireNotFoundFragment
import com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments.main.SharesOfAssetsState
import com.example.financialwunderwaffe.retrofit.database.calculationShareOfAsset.shareOfAsset.CalculationShareOfAsset
import com.example.financialwunderwaffe.retrofit.database.calculationShareOfAsset.shareOfAsset.CalculationShareOfAssetApiClient
import com.example.financialwunderwaffe.retrofit.request.RequestTemplate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
//        temporaryStub()
//        goToFragment(briefcaseSharesOfAssetsMainFragment)

        return view
    }

    fun goToFragment(fragment: Fragment) {
        val briefcaseSharesOfAssets = childFragmentManager.beginTransaction()
        briefcaseSharesOfAssets.replace(R.id.container_briefcase_shares_of_assets, fragment)
        briefcaseSharesOfAssets.addToBackStack(null)
        briefcaseSharesOfAssets.commit()
    }

    private fun requestGetSharesOfAssets() {
        CalculationShareOfAssetApiClient.calculationShareOfAssetApiService.getCalculationShareOfAssetByUID(
            (activity as MainActivity).basicLoginAndPassword,
            (activity as MainActivity).uid
        ).enqueue(object : Callback<RequestTemplate<CalculationShareOfAsset>> {
            override fun onResponse(
                call: Call<RequestTemplate<CalculationShareOfAsset>>,
                response: Response<RequestTemplate<CalculationShareOfAsset>>
            ) {
                println(response)
                if (response.isSuccessful && response.body() != null && response.code() == 200) {
                    val answer = response.body()
                    println(answer)
                    if (answer == null) {
                        (activity as MainActivity).toast("Непредвиденная ошибка на стороне сервера")
                        goToFragment(briefcaseSharesOfAssetsQuestionnaireNotFound)
                    } else if (answer.status.code == 422 && answer.status.message == "Questionnaire Not Found") {
                        goToFragment(briefcaseSharesOfAssetsQuestionnaireNotFound)
                    } else if (answer.status.code == 200 && answer.data != null){
                        briefcaseSharesOfAssetsMainFragment.listStateSharesOfAssetsState =
                            answer.data.listShareOfAsset.map {
                                SharesOfAssetsState(
                                    typeAssets = it.typeAsset.name,
                                    now = 10000, // TODO: придумать откуда брать начальное значение доли актива
                                    goal = it.share,
                                    delta = it.share - 10000,
                                    color = it.typeAsset.color,
                                )
                            }
                        println(briefcaseSharesOfAssetsMainFragment.listStateSharesOfAssetsState)
                        goToFragment(briefcaseSharesOfAssetsMainFragment)
                    } else {
                        loadingFragment.text = answer.status.message
                        loadingFragment.visible = "false"
                        goToFragment(loadingFragment)
                    }
                } else {
                    (activity as MainActivity).toast("Ошибка сервера: ${response.code()} - ${response.message()}")
                    loadingFragment.text = response.message()
                    loadingFragment.visible = "false"
                    goToFragment(loadingFragment)
                }
            }

            override fun onFailure(call: Call<RequestTemplate<CalculationShareOfAsset>>, t: Throwable) {
                (activity as MainActivity).toast("Ошибка сети: ${t.message}")
                loadingFragment.text = t.message.toString()
                loadingFragment.visible = "false"
                goToFragment(loadingFragment)
            }
        })
    }

    private fun temporaryStub() {
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
    }

}