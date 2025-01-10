package com.example.financialwunderwaffe.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.database.AppDatabase
import com.example.financialwunderwaffe.databinding.ActivityMainBinding
import com.example.financialwunderwaffe.welcome.WelcomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val context = this
    lateinit var basicLoginAndPassword: String
    lateinit var uid: UUID
    val date: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.container_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_budget, R.id.navigation_goal, R.id.navigation_briefcase, R.id.navigation_LK
            )
        )

        CoroutineScope(Dispatchers.IO).launch {
            val info = AppDatabase.getDatabase(context).getUserInfoDao().getInfo()[0]
            basicLoginAndPassword = "Basic " + info.loginAndPassword
            uid = info.uid
        }

//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    fun goToWelcomeActivity() =
        startActivity(Intent(this, WelcomeActivity::class.java))

}