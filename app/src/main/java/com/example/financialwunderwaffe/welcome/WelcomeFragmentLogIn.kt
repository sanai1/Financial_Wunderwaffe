package com.example.financialwunderwaffe.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.financialwunderwaffe.data.ApiClient
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.data.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Base64

class WelcomeFragmentLogIn : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_welcome_log_in, container, false)

        val back : TextView = view.findViewById(R.id.textViewBackLogIn)
        back.setOnClickListener {
            (activity as WelcomeActivity).back()
        }

        val email : EditText = view.findViewById(R.id.editTextTextEmailAddressLogIn)
        val password : EditText = view.findViewById(R.id.editTextNumberPasswordLogIn)


        val resume : Button = view.findViewById(R.id.buttonResumeLogIn)

        resume.setOnClickListener {
            val s : String = email.text.toString() + ":" + password.text.toString()
            val bytes = s.toByteArray()
            val auth : String = "Basic " + Base64.getEncoder().encodeToString(bytes)
            println(auth)

            if (email.text.isEmpty())
                (activity as WelcomeActivity).toast("Введите email")
            else if (password.text.length < 6)
                (activity as WelcomeActivity).toast("Минимальная длина пароля 6 цифр")
            else {
                ApiClient.apiService.findByLogin(auth, email.text.toString()).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            val uid = response.body()
                            println("UID пользователя: $uid")
                            (activity as WelcomeActivity).log_in(email.text.toString(), password.text.toString().toInt())
                        } else {
                            (activity as WelcomeActivity).toast("Ошибка сервера: ${response.code()} - ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        (activity as WelcomeActivity).toast("Ошибка сети: ${t.message}")
                    }
                })
            }
        }

        return view
    }


}