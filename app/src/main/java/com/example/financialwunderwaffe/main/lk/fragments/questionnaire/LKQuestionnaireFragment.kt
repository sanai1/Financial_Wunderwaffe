package com.example.financialwunderwaffe.main.lk.fragments.questionnaire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.lk.LKFragment
import com.example.financialwunderwaffe.retrofit.database.questionnaire.question.Question
import com.example.financialwunderwaffe.retrofit.database.questionnaire.user_answer.UserAnswer
import com.example.financialwunderwaffe.retrofit.database.questionnaire.user_answer.UserAnswerApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LKQuestionnaireFragment : Fragment() {

    private lateinit var textViewQuestion: TextView
    private lateinit var listRadioButton: List<RadioButton>
    private lateinit var radioGroup: RadioGroup
    private lateinit var imageViewBack: ImageView
    private lateinit var imageViewFurther: ImageView
    private lateinit var buttonSave:  Button
    private var indexQuestion = 0
    private var isFirstResponse = true
    var listUserAnswer = mutableListOf<UserAnswer>()
    var listQuestionsAndAnswers = listOf<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_lk_questionnaire, container, false)

        textViewQuestion = view.findViewById(R.id.textViewQuestion)
        listRadioButton = listOf(
            view.findViewById(R.id.radioButton1),
            view.findViewById(R.id.radioButton2),
            view.findViewById(R.id.radioButton3),
            view.findViewById(R.id.radioButton4)
        )
        radioGroup = view.findViewById(R.id.radioGroupQuestionnaire)

        imageViewBack = view.findViewById(R.id.imageViewBackArrow)
        imageViewBack.isVisible = false
        imageViewBack.setOnClickListener {
            backQuestion()
        }

        imageViewFurther = view.findViewById(R.id.imageViewFurtherArrow)
        imageViewFurther.isVisible = true
        imageViewFurther.setOnClickListener {
            furtherQuestion()
        }

        buttonSave = view.findViewById(R.id.buttonSaveQuestionnaire)
        buttonSave.setOnClickListener {
            saveQuestionnaire()
        }

        view.findViewById<ImageView>(R.id.imageViewWelcome).setOnClickListener {
            backToMainFragmentLK()
        }

        if (listUserAnswer.isNotEmpty()) {
            isFirstResponse = false
        }

        if (listQuestionsAndAnswers.isNotEmpty()) {
            if (listQuestionsAndAnswers.size == 1) imageViewFurther.isVisible = false
            updateQuestion()
        }

        return view
    }

    private fun backQuestion() {
        indexQuestion--
        imageViewFurther.isVisible = true
        buttonSave.isVisible = false
        if (indexQuestion == 0) {
            imageViewBack.isVisible = false
        } else {
            imageViewBack.isVisible = true
        }
        updateQuestion()
        try {
            listUserAnswer[indexQuestion+1]
        } catch (_: Exception) {
            listUserAnswer.removeAt(listUserAnswer.lastIndex)
        }
    }

    private fun furtherQuestion() {
        if (radioGroup.checkedRadioButtonId == -1) {
            (activity as MainActivity).toast("Выберите вариант ответа")
            return
        }
        indexQuestion++
        imageViewBack.isVisible = true
        if (indexQuestion == listQuestionsAndAnswers.size - 1)  {
            imageViewFurther.isVisible = false
            buttonSave.isVisible = true
        } else {
            buttonSave.isVisible = false
            imageViewFurther.isVisible = true
        }
        try {
            listUserAnswer[indexQuestion-1]
            updateUserAnswer()
        } catch (_: Exception) {
            addUserAnswer()
        }
        updateQuestion()
    }

    private fun addUserAnswer() {
        when (radioGroup.checkedRadioButtonId) {
            listRadioButton[0].id -> listUserAnswer.add(getUserAnswer(0))
            listRadioButton[1].id -> listUserAnswer.add(getUserAnswer(1))
            listRadioButton[2].id -> listUserAnswer.add(getUserAnswer(2))
            listRadioButton[3].id -> listUserAnswer.add(getUserAnswer(3))
        }
    }

    private fun updateUserAnswer() {
        when (radioGroup.checkedRadioButtonId) {
            listRadioButton[0].id -> listUserAnswer[indexQuestion-1] = getUserAnswer(0)
            listRadioButton[1].id -> listUserAnswer[indexQuestion-1] = getUserAnswer(1)
            listRadioButton[2].id -> listUserAnswer[indexQuestion-1] = getUserAnswer(2)
            listRadioButton[3].id -> listUserAnswer[indexQuestion-1] = getUserAnswer(3)
        }
    }

    private fun getUserAnswer(id: Long): UserAnswer =
        UserAnswer(
            id = try {
                listUserAnswer[indexQuestion-1].id
            } catch (_: Exception) {
                0L
            },
            userUID = (activity as MainActivity).uid,
            questionID = listQuestionsAndAnswers[indexQuestion-1].id,
            answerID = listQuestionsAndAnswers[indexQuestion-1].listAnswers[id.toInt()].id,
            date = (activity as MainActivity).date.toString()
        )

    private fun updateQuestion() {
        if (listQuestionsAndAnswers[indexQuestion].isEnabled) {
            textViewQuestion.text = listQuestionsAndAnswers[indexQuestion].text
            var indexAnswer = 0
            listRadioButton.map {
                it.text = listQuestionsAndAnswers[indexQuestion].listAnswers[indexAnswer++].text
            }
            try {
                when(listUserAnswer[indexQuestion].answerID % 4) {
                    1L -> listRadioButton[0].isChecked = true
                    2L -> listRadioButton[1].isChecked = true
                    3L -> listRadioButton[2].isChecked = true
                    0L -> listRadioButton[3].isChecked = true
                }
            } catch (_: Exception) {
                radioGroup.clearCheck()
            }
        } else {
            furtherQuestion()
        }
    }

    private fun saveQuestionnaire() {
        if (radioGroup.checkedRadioButtonId == -1) {
            (activity as MainActivity).toast("Выберите вариант ответа")
            return
        }
        indexQuestion++
        try {
            listUserAnswer[indexQuestion-1]
            updateUserAnswer()
        } catch (_: Exception) {
            addUserAnswer()
        }
        (parentFragment as LKFragment).goToFragment(
            (parentFragment as LKFragment).loadingFragment
        )
        if (isFirstResponse) {
            requestSaveUserAnswer()
        } else {
            requestUpdateUserAnswer()
        }
        updateThisFragment()
    }

    private fun requestSaveUserAnswer() =
        UserAnswerApiClient.userAnswerAPIService.createUserAnswer(
            (activity as MainActivity).basicLoginAndPassword,
            listUserAnswer
        ).enqueue(object : Callback<List<Long>> {
            override fun onResponse(call: Call<List<Long>>, response: Response<List<Long>>) {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    if (list.isEmpty())
                        (activity as MainActivity).toast("Произошла ошибка сохранения данных")
                    backToMainFragmentLK()
                } else {
                    (activity as MainActivity).toast("Ошибка сервера: ${response.code()} - ${response.message()}")
                    backToQuestionnaireFragmentLK()
                }
            }

            override fun onFailure(call: Call<List<Long>>, t: Throwable) {
                (activity as MainActivity).toast("Ошибка сети: ${t.message}")
                backToQuestionnaireFragmentLK()
            }

        })

    private fun requestUpdateUserAnswer() =
        UserAnswerApiClient.userAnswerAPIService.updateUserAnswer(
            (activity as MainActivity).basicLoginAndPassword,
            listUserAnswer
        ).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    if (response.body() == false)
                        (activity as MainActivity).toast("Произошла ошибка обновления данных")
                    backToMainFragmentLK()
                } else {
                    (activity as MainActivity).toast("Ошибка сервера: ${response.code()} - ${response.message()}")
                    backToQuestionnaireFragmentLK()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                (activity as MainActivity).toast("Ошибка сети: ${t.message}")
                backToQuestionnaireFragmentLK()
            }

        })

    private fun backToMainFragmentLK() {
        (parentFragment as LKFragment).goToFragment(
            (parentFragment as LKFragment).lkMainFragment
        )
        updateThisFragment()
    }

    private fun backToQuestionnaireFragmentLK() {
        (parentFragment as LKFragment).goToFragment(
            (parentFragment as LKFragment).lkQuestionnaireFragment // TODO: проверить можно ли при ошибке воозвращаться в анкету
        )
        updateThisFragment()
    }

    private fun updateThisFragment() {
        (parentFragment as LKFragment).lkQuestionnaireFragment = LKQuestionnaireFragment()
    }

}