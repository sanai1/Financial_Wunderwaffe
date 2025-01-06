package com.example.financialwunderwaffe.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.financialwunderwaffe.LoadingFragment
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.database.AppDatabase
import com.example.financialwunderwaffe.database.UserInfo
import com.example.financialwunderwaffe.databinding.ActivityWelcomeBinding
import com.example.financialwunderwaffe.retrofit.database.user.User
import com.example.financialwunderwaffe.retrofit.database.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    lateinit var codeFragment: WelcomeFragmentCode

    val context = this
    var code: Long = 0L
    var uid: UUID? = null
    var login = ""
    var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding_main = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding_main.root)

        startFragment = WelcomeFragmentStart()
        logInFragment = WelcomeFragmentLogIn()
        registrationFragment = WelcomeFragmentRegistration()
        loadingFragment = LoadingFragment()
        codeFragment = WelcomeFragmentCode()

        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, loadingFragment).commit()

        CoroutineScope(Dispatchers.IO).launch {
            checkAuth()
        }
    }

    private fun checkAuth() {
        val list = AppDatabase.getDatabase(this).getUserInfoDao().getInfo()
        if (list.isEmpty()) {
            supportFragmentManager.beginTransaction().replace(R.id.container_welcome, startFragment).commit()
            return
        }
        code = list.last().code
        codeFragment.checkCode = true
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, codeFragment).commit()
    }

    fun goToLogIn() =
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, logInFragment).commit()

    fun goToRegistration() =
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, registrationFragment).commit()

    fun goToMainActivity() =
        startActivity(Intent(this, MainActivity::class.java))

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
                    goToInstallCode(UUID.fromString(response.body()), email, password)
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
                    goToInstallCode(uid, email, password)
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

    private fun goToInstallCode(uid_: UUID, login_: String, password_: String) {
        uid = uid_
        login = login_
        password = password_
        codeFragment.checkCode = false
        supportFragmentManager.beginTransaction().replace(R.id.container_welcome, codeFragment).commit()
    }

    fun saveUID(code: String) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase(context).getUserInfoDao().insertUserInfo(
                UserInfo(
                    id = 1,
                    uid = uid!!,
                    loginAndPassword = Base64.getEncoder()
                        .encodeToString("$login:$password".toByteArray()),
                    code = code.toLong()
                )
            )
            goToMainActivity()
        }
    }

    fun deleteUID() {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase(context).getUserInfoDao().deleteUserInfo(
                AppDatabase.getDatabase(context).getUserInfoDao().getInfo()[0]
            )
        }
    }

}