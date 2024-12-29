package com.example.financialwunderwaffe.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.financialwunderwaffe.R

class WelcomeFragmentStart : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_welcome_start, container, false)

        val logIn : Button = view.findViewById(R.id.button_log_in)
        logIn.setOnClickListener {
            (activity as WelcomeActivity).goToLogIn()
        }

        val registration : Button = view.findViewById(R.id.button_register)
        registration.setOnClickListener {
            (activity as WelcomeActivity).goToRegistration()
        }

        return view
    }

}