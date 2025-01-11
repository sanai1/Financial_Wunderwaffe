package com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.financialwunderwaffe.R

class BriefcaseSharesOfAssetsQuestionnaireNotFoundFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_briefcase_shares_of_assets_questionnaire_not_found, container, false)

        return view
    }

}