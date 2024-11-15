package com.example.financialwunderwaffe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.financialwunderwaffe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding_main : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding_main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding_main.root)

    }

    fun test(v: View) {
        println("test click")
    }

}