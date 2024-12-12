package com.example.financialwunderwaffe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.financialwunderwaffe.Fragments.WelcomeFragmentLogIn
import com.example.financialwunderwaffe.Fragments.WelcomeFragmentRegistration
import com.example.financialwunderwaffe.Fragments.WelcomeFragmentStart
import com.example.financialwunderwaffe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding_main : ActivityMainBinding
    lateinit var startFragment : WelcomeFragmentStart
    lateinit var logInFragment : WelcomeFragmentLogIn
    lateinit var registrationFragment : WelcomeFragmentRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding_main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding_main.root)

        startFragment = WelcomeFragmentStart()
        logInFragment = WelcomeFragmentLogIn()
        registrationFragment = WelcomeFragmentRegistration()

        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, startFragment).commit()

    }

    fun log_in() {
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, logInFragment).commit()
    }

    fun registration() {
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, registrationFragment).commit()
    }


}