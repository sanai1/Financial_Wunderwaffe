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

class WelcomeFragmentLogIn : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view : View = inflater.inflate(R.layout.fragment_welcome_log_in, container, false)

        val back : TextView = view.findViewById(R.id.textViewRegister)
        back.setOnClickListener {
            (activity as WelcomeActivity).goToRegistration()
        }

        val email : EditText = view.findViewById(R.id.editTextTextEmailAddressLogIn)
        val password : EditText = view.findViewById(R.id.editTextPasswordLogIn)

        val resume : Button = view.findViewById(R.id.buttonResumeLogIn)
        resume.setOnClickListener {
            if (email.text.isEmpty())
                (activity as WelcomeActivity).toast("Введите email")
            else if (password.text.length < 6)
                (activity as WelcomeActivity).toast("Минимальная длина пароля 6 цифр")
            else {
                (activity as WelcomeActivity).logIn(email = email.text.toString(), password = password.text.toString())
            }
        }

        return view
    }

}