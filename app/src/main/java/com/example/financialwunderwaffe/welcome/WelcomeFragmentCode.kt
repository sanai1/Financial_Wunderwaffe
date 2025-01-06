package com.example.financialwunderwaffe.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.financialwunderwaffe.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WelcomeFragmentCode : Fragment() {

    var checkCode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome_code, container, false)

        val numberCode: EditText = view.findViewById(R.id.editTextNumberCode)

        val buttonCode: Button = view.findViewById(R.id.buttonCode)
        if (checkCode)
            buttonCode.setOnClickListener {
                if (numberCode.text.length < 4) {
                    (activity as WelcomeActivity).toast("Введите код из 4 цифр")
                } else if ((activity as WelcomeActivity).code == numberCode.text.toString().toLong()) {
                    (activity as WelcomeActivity).goToMainActivity()
                } else {
                    (activity as WelcomeActivity).toast("Введен неверный код")
                }
            }
        else
            buttonCode.setOnClickListener {
                if (numberCode.text.length < 4) {
                    (activity as WelcomeActivity).toast("Введите код из 4 цифр")
                } else {
                    (activity as WelcomeActivity).saveUID(numberCode.text.toString())
                }
            }

        val reset: TextView = view.findViewById(R.id.textViewReset)
        if (checkCode) {
            reset.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    (activity as WelcomeActivity).deleteUID()
                }
                (activity as WelcomeActivity).goToLogIn()
            }
            reset.isVisible = true
        }
        else
            reset.isVisible = false

        return view
    }

}