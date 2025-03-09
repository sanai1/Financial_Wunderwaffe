package com.example.financialwunderwaffe.main.budget.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.retrofit.database.category.Category
import com.example.financialwunderwaffe.retrofit.database.category.CategoryApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Base64
import java.util.UUID

class BudgetCategoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_budget_category, container, false)

        val s: String = "7852057a-ecf6-40ba-b3eb-aad3d6be818f"
        val uuid = UUID.fromString(s)
        val email = "siuuu"
        val password = "123456"
        val base = "Basic " + Base64.getEncoder().encodeToString("$email:$password".toByteArray())

        var id : Long = 1
        var name : EditText = view.findViewById(R.id.category_input)
        var type: Boolean = true

        val resume : Button = view.findViewById(R.id.button_add_category)
        resume.setOnClickListener {
            CategoryApiClient.categoryAPIService.createCategory(
                base,
                Category(
//                    id = id,
                    name = name.text.toString(),
                    type = type,
                    userUID = s
                )
            ).enqueue(object : Callback<Long> {
                override fun onResponse(call: Call<Long>, response: Response<Long>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Категория успешно создана!", Toast.LENGTH_SHORT)
                            .show()

                        val fragmentManager = parentFragmentManager
                        val currentFragment =
                            fragmentManager.findFragmentByTag(this@BudgetCategoryFragment::class.java.simpleName)

                        if (currentFragment != null) {
                            fragmentManager.beginTransaction()
                                .detach(currentFragment)
                                .attach(currentFragment)
                                .commit()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
//                        Log.e("Response Error", "Код: ${response.code()}, Тело: $errorBody")
//                        Toast.makeText(context, "Ошибка: $errorBody", Toast.LENGTH_LONG).show()

                        Toast.makeText(
                            context,
                            "Ошибка создания категории: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Long>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Не удалось создать категорию: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("BudgetCategoryFragment", "Ошибка: ${t.message}", t)
                }
            })
        }

        return view
    }

}