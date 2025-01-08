package com.example.financialwunderwaffe.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.financialwunderwaffe.R

class WelcomeFragmentRegistration : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view : View = inflater.inflate(R.layout.fragment_welcome_registration, container, false)

        val back : TextView = view.findViewById(R.id.textViewLogIn)
        back.setOnClickListener {
            (activity as WelcomeActivity).goToLogIn()
        }

        val email : EditText = view.findViewById(R.id.editTextTextEmailAddressRegister)
        val password : EditText = view.findViewById(R.id.editTextNumberPasswordRegister)
        val passwordAgain : EditText = view.findViewById(R.id.editTextNumberPasswordRegisterPasswordAgain)

        val resume : Button = view.findViewById(R.id.buttonResumeRegister)
        resume.setOnClickListener {
            if (email.text.isEmpty()) {
                (activity as WelcomeActivity).toast("Введите email")
            } else if (password.text.toString().length < 6) {
                (activity as WelcomeActivity).toast("Минимальная длина пароля 6 цифр")
            } else if (password.text.toString() != passwordAgain.text.toString()) {
                (activity as WelcomeActivity).toast("Пароли не совпадают")
            } else {
                (activity as WelcomeActivity).registration(email.text.toString(), password.text.toString())
            }
        }

        return view
    }

}