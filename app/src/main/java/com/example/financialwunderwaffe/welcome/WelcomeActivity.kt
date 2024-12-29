package com.example.financialwunderwaffe.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.financialwunderwaffe.LoadingFragment
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.databinding.ActivityWelcomeBinding
import com.example.financialwunderwaffe.retrofit.database.user.User
import com.example.financialwunderwaffe.retrofit.database.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Base64
import java.util.UUID

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding_main : ActivityWelcomeBinding
    lateinit var startFragment : WelcomeFragmentStart
    lateinit var logInFragment : WelcomeFragmentLogIn
    lateinit var registrationFragment : WelcomeFragmentRegistration
    lateinit var loadingFragment: LoadingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding_main = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding_main.root)

        startFragment = WelcomeFragmentStart()
        logInFragment = WelcomeFragmentLogIn()
        registrationFragment = WelcomeFragmentRegistration()
        loadingFragment = LoadingFragment()

        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, startFragment).commit()

    }

    fun goToLogIn() =
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, logInFragment).commit()

    fun goToRegistration() =
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, registrationFragment).commit()

    fun back() =
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, startFragment).commit()

    private fun loading() =
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, loadingFragment).commit()

    fun toast(text : String) =
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()

    fun logIn(email : String, password : String) {
        loading()
        UserApiClient.userAPIService.findByLogin(
            "Basic " + Base64.getEncoder().encodeToString("$email:$password".toByteArray()),
            email
        ).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    saveUID(UUID.fromString(response.body()))
                    goToMainActivity()
                } else {
                    toast("Ошибка сервера: ${response.code()} - ${response.message()}")
                    goToLogIn()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                toast("Ошибка сети: ${t.message}")
                goToLogIn()
            }
        })
    }

    fun registration(email : String, password: String) {
        loading()
        val uid = UUID.randomUUID()
        UserApiClient.userAPIService.registerUser(
            User(
                uid = uid.toString(),
                login = email,
                password = password,
            )
        ).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    saveUID(uid)
                    goToMainActivity()
                } else {
                    toast("Ошибка сервера: ${response.code()} - ${response.message()}")
                    goToRegistration()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                toast("Ошибка сети: ${t.message}")
                goToRegistration()
            }
        })
    }

    private fun saveUID(uid: UUID) {
        println(uid)
        // TODO: сохранить UID пользователя для дальнейших запросов
    }

    private fun goToMainActivity() =
        startActivity(Intent(this, MainActivity::class.java))

}