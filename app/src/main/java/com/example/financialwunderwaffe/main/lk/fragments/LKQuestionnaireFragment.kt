package com.example.financialwunderwaffe.main.lk.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.lk.LKFragment
import com.example.financialwunderwaffe.retrofit.database.questionnaire.model.Question

class LKQuestionnaireFragment(
    private var listQuestionsAndAnswers: List<Question>
) : Fragment() {

    private lateinit var textViewQuestion: TextView
    private lateinit var listRadioButton: List<RadioButton>
    private lateinit var imageViewBack: ImageView
    private lateinit var imageViewFurther: ImageView
    private lateinit var buttonSave:  Button
    private var indexQuestion = 0
//    private lateinit var listUserAnswers  TODO:

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
    }

    private fun furtherQuestion() {
        indexQuestion++
        imageViewBack.isVisible = true
        if (indexQuestion == listQuestionsAndAnswers.size - 1)  {
            imageViewFurther.isVisible = false
            buttonSave.isVisible = true
        } else {
            buttonSave.isVisible = false
            imageViewFurther.isVisible = true
        }
        updateQuestion()
    }

    private fun updateQuestion() {
        if (listQuestionsAndAnswers.get(indexQuestion).isEnabled) {
            textViewQuestion.text = listQuestionsAndAnswers.get(indexQuestion).text
            var indexAnswer = 0
            listRadioButton.map {
                it.text = listQuestionsAndAnswers.get(indexQuestion).listAnswers.get(indexAnswer++).text
            }
        } else {
            furtherQuestion()
        }
    }

    private fun saveQuestionnaire() {
        (activity as MainActivity).toast("Данная кнопка в разработке")
    }

    private fun backToMainFragmentLK() =
        (parentFragment as LKFragment).goToFragment(
            (parentFragment as LKFragment).lkMainFragment
        )

}