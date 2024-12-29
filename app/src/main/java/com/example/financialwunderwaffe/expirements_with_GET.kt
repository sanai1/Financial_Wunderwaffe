package com.example.financialwunderwaffe

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.financialwunderwaffe.data.ApiClient
import com.example.financialwunderwaffe.data.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = "user@example.com"
        val password = "password123"

//        ApiClient.apiService.checkUser(email, password).enqueue(object : Callback<ResponseModel> {
//            override fun onResponse(
//                call: Call<ResponseModel>,
//                response: Response<ResponseModel>
//            ) {
//                if (response.isSuccessful && response.body()?.success == true) {
//                    Toast.makeText(this@SomeActivity, "User verified!", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this@SomeActivity, "Verification failed.", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
//                Toast.makeText(this@SomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
    }
}
