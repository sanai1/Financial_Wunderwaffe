package com.example.financialwunderwaffe.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.financialwunderwaffe.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WelcomeFragmentCode : Fragment() {

    var checkCode = true
    lateinit var numberCode: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome_code, container, false)

        numberCode = view.findViewById(R.id.editTextNumberCode)

        val reset: TextView = view.findViewById(R.id.textViewReset)
        if (checkCode) {
            reset.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    (activity as WelcomeActivity).deleteUID()
                }
                (activity as WelcomeActivity).goToLogIn()
            }
            view.findViewById<LinearLayout>(R.id.linearLayoutCode).isVisible = true
        }
        else
            view.findViewById<LinearLayout>(R.id.linearLayoutCode).isVisible = false

        val buttons: List<Button> = listOf(
            view.findViewById(R.id.buttonCode0),
            view.findViewById(R.id.buttonCode1),
            view.findViewById(R.id.buttonCode2),
            view.findViewById(R.id.buttonCode3),
            view.findViewById(R.id.buttonCode4),
            view.findViewById(R.id.buttonCode5),
            view.findViewById(R.id.buttonCode6),
            view.findViewById(R.id.buttonCode7),
            view.findViewById(R.id.buttonCode8),
            view.findViewById(R.id.buttonCode9)
        )
        buttons.forEach {
            it.setOnClickListener { button ->
                onButtonNumberClick(it)
            }
        }

        view.findViewById<Button>(R.id.buttonCodeDEL).setOnClickListener {
            if (numberCode.text.length > 0) {
                numberCode.setText(numberCode.text.substring(0, numberCode.text.length - 1))
            }
        }

        view.findViewById<Button>(R.id.buttonCodeGO).setOnClickListener {
            if (checkCode) {
                if (numberCode.text.length < 4) {
                    (activity as WelcomeActivity).toast("Введите код из 4 цифр")
                } else if ((activity as WelcomeActivity).code == numberCode.text.toString().toLong()) {
                    (activity as WelcomeActivity).goToMainActivity()
                } else {
                    (activity as WelcomeActivity).toast("Введен неверный код")
                }
            } else {
                if (numberCode.text.length < 4) {
                    (activity as WelcomeActivity).toast("Введите код из 4 цифр")
                } else {
                    (activity as WelcomeActivity).saveUID(numberCode.text.toString())
                }
            }
        }

        return view
    }

    private fun onButtonNumberClick(button: Button) {
        if (numberCode.text.length < 4) {
            val newText = numberCode.text.toString() + button.text.toString()
            numberCode.setText(newText)
        }
    }

}