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
import com.example.financialwunderwaffe.data.User
import java.util.Base64
import java.util.UUID

class WelcomeFragmentRegistration : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_welcome_registration, container, false)

        val back : TextView = view.findViewById(R.id.textViewBackRegister)
        back.setOnClickListener {
            (activity as WelcomeActivity).back()
        }

        val email : EditText = view.findViewById(R.id.editTextTextEmailAddressRegister)
        val password : EditText = view.findViewById(R.id.editTextNumberPasswordRegister)
        val password_again : EditText = view.findViewById(R.id.editTextNumberPasswordRegisterPasswordAgain)

        val resume : Button = view.findViewById(R.id.buttonResumeRegister)
        resume.setOnClickListener {
            if (email.text.isEmpty()) {
                (activity as WelcomeActivity).toast("Введите email")
            } else if (password.text.toString().length < 6) {
                (activity as WelcomeActivity).toast("Минимальная длина пароля 6 цифр")
            } else if (password.text.toString() != password_again.text.toString()) {
                (activity as WelcomeActivity).toast("Пароли не совпадают")
            } else {
                try {
                    val uuid = UUID.randomUUID()

                    val user = User(
                        uid = uuid.toString(),
                        login = email.text.toString(),
                        password = password.text.toString(),
                        authority = "USER"
                    )

                    val s : String = "kost" + ":" + "123456"
                    val bytes = s.toByteArray()
                    val auth : String = "Basic " + Base64.getEncoder().encodeToString(bytes)
                    println(auth)

                    ApiClient.apiService.registerUser(auth, user).enqueue(object : Callback<ResponseModel> {
                        override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                            if (response.isSuccessful) {
                                println(response)
                                (activity as WelcomeActivity).registration(email.text.toString(), password.text.toString().toInt())
                            } else {
                                (activity as WelcomeActivity).toast("Ошибка сервера: ${response.code()} - ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                            (activity as WelcomeActivity).toast("Ошибка сети: ${t.message}")
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    (activity as WelcomeActivity).toast("Произошла ошибка: ${e.message}")
                    print("Произошла ошибка: ${e.message}")
                }
            }
        }

        return view
    }

}