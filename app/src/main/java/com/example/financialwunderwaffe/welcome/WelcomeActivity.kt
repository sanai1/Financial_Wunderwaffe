package com.example.financialwunderwaffe.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding_main : ActivityWelcomeBinding
    lateinit var startFragment : WelcomeFragmentStart
    lateinit var logInFragment : WelcomeFragmentLogIn
    lateinit var registrationFragment : WelcomeFragmentRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding_main = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding_main.root)

        startFragment = WelcomeFragmentStart()
        logInFragment = WelcomeFragmentLogIn()
        registrationFragment = WelcomeFragmentRegistration()

        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, startFragment).commit()

    }

    fun go_to_log_in() {
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, logInFragment).commit()
    }

    fun go_to_registration() {
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, registrationFragment).commit()
    }

    fun back() {
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, startFragment).commit()
    }

    fun toast(text : String) {
        val toast : Toast = Toast.makeText(this, text, Toast.LENGTH_LONG)
        toast.show()
    }

    fun log_in(email : String, password : Int) {
        // TODO("выполнить вход в приложение на сервере -> выкачать данные с сервера на клиент офлайн")
        go_to_main_activity()
    }

    fun registration(email : String, password: Int) {
        // TODO("выполнить регистрацию нового пользователя на сервере")
        go_to_main_activity()
    }

    private fun go_to_main_activity() {
        val intent : Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}