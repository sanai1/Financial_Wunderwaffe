package com.example.financialwunderwaffe.main.lk.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.database.AppDatabase
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.lk.LKFragment
import com.example.financialwunderwaffe.main.lk.fragments.main.dialogs.LKDialogExitMainFragment
import com.example.financialwunderwaffe.retrofit.database.questionnaire.question.QuestionApiClient
import com.example.financialwunderwaffe.retrofit.database.questionnaire.question.Question
import com.example.financialwunderwaffe.retrofit.database.questionnaire.userAnswer.UserAnswer
import com.example.financialwunderwaffe.retrofit.database.questionnaire.userAnswer.UserAnswerApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LKMainFragment :
    Fragment(),
    LKDialogExitMainFragment.DialogExitMainFragment
{

    private lateinit var dialogExitMainFragment: LKDialogExitMainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lk_main, container, false)

        dialogExitMainFragment = LKDialogExitMainFragment()
        dialogExitMainFragment.setDialogExitMainFragment(this)

        view.findViewById<LinearLayout>(R.id.linearLayoutEmailLK).setOnClickListener {
            emailLK()
        }

        view.findViewById<LinearLayout>(R.id.linearLayoutTestLK).setOnClickListener {
            testLK()
        }

        view.findViewById<ImageView>(R.id.imageViewExitLK).setOnClickListener {
            exitLK()
        }

        view.findViewById<TextView>(R.id.textViewExitLK).setOnClickListener {
            exitLK()
        }

        return view
    }

    private fun emailLK() {
        (activity as MainActivity).toast("Этот функционал скоро станет доступным")
    }

    private fun testLK() {
        (parentFragment as LKFragment).goToFragment(
            (parentFragment as LKFragment).loadingFragment
        )
        requestGetQuestion()
    }

    private fun requestGetQuestion() =
        QuestionApiClient.questionAPIService.getQuestions(
            (activity as MainActivity).basicLoginAndPassword
        ).enqueue(object : Callback<List<Question>> {
            override fun onResponse(
                call: Call<List<Question>>,
                response: Response<List<Question>>
            ) {
                if (response.isSuccessful) {
                    (parentFragment as LKFragment).lkQuestionnaireFragment.listQuestionsAndAnswers =
                        response.body() ?: emptyList()
                    requestGetUserAnswer()
                } else {
                    (activity as MainActivity).toast("Ошибка сервера: ${response.code()} - ${response.message()}")
                    (parentFragment as LKFragment).goToFragment(
                        (parentFragment as LKFragment).lkMainFragment
                    )
                }
            }

            override fun onFailure(call: Call<List<Question>>, t: Throwable) {
                (activity as MainActivity).toast("Ошибка сети: ${t.message}")
                (parentFragment as LKFragment).goToFragment(
                    (parentFragment as LKFragment).lkMainFragment
                )
            }

        })

    private fun requestGetUserAnswer() =
        UserAnswerApiClient.userAnswerAPIService.getUserAnswerByUID(
            (activity as MainActivity).basicLoginAndPassword,
            (activity as MainActivity).uid
        ).enqueue(object : Callback<List<UserAnswer>> {
            override fun onResponse(
                call: Call<List<UserAnswer>>,
                response: Response<List<UserAnswer>>
            ) {
                if (response.isSuccessful) {
                    (parentFragment as LKFragment).lkQuestionnaireFragment.listUserAnswer =
                        (response.body() ?: emptyList()).toMutableList()
                    (parentFragment as LKFragment).goToFragment(
                        (parentFragment as LKFragment).lkQuestionnaireFragment
                    )
                } else {
                    (activity as MainActivity).toast("Ошибка сервера: ${response.code()} - ${response.message()}")
                    (parentFragment as LKFragment).goToFragment(
                        (parentFragment as LKFragment).lkMainFragment
                    )
                }
            }

            override fun onFailure(call: Call<List<UserAnswer>>, t: Throwable) {
                (activity as MainActivity).toast("Ошибка сети: ${t.message}")
                (parentFragment as LKFragment).goToFragment(
                    (parentFragment as LKFragment).lkMainFragment
                )
            }

        })

    private fun exitLK() =
        dialogExitMainFragment.show(childFragmentManager, "dialogExitMainFragment")

    override fun onDialogExitMainFragmentClickListener() {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase((activity as MainActivity).context).getUserInfoDao().deleteUserInfo(
                AppDatabase.getDatabase((activity as MainActivity).context).getUserInfoDao().getInfo()[0]
            )
            (activity as MainActivity).goToWelcomeActivity()
        }
    }

}