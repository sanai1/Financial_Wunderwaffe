package com.example.financialwunderwaffe.main

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.database.AppDatabase
import com.example.financialwunderwaffe.databinding.ActivityMainBinding
import com.example.financialwunderwaffe.main.analytics.AnalyticsViewModel
import com.example.financialwunderwaffe.main.briefcase.BriefcaseViewModel
import com.example.financialwunderwaffe.main.budget.BudgetViewModel
import com.example.financialwunderwaffe.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    val printToast = fun(message: String) {
        toast(message)
    }
    val briefcaseViewModel = BriefcaseViewModel()
    val analyticsViewModel = AnalyticsViewModel()
    val budgetViewModel = BudgetViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT.also { requestedOrientation = it }

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
            budgetViewModel.apply {
                updateCategory(basicLoginAndPassword, uid)
            }
            analyticsViewModel.apply {
                updateBudgetByMonth(basicLoginAndPassword, uid)
                updateCapitalByMonth(basicLoginAndPassword, uid)
            }
        }

//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    fun goToWelcomeActivity() =
        startActivity(Intent(this, WelcomeActivity::class.java))

}