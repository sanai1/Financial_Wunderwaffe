package com.example.financialwunderwaffe.main.budget.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.retrofit.database.user.UserApiClient
import com.example.financialwunderwaffe.retrofit.database.transaction.Transaction
import com.example.financialwunderwaffe.retrofit.database.transaction.TransactionAdapter
import com.example.financialwunderwaffe.retrofit.database.transaction.TransactionApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Base64
import java.util.UUID

private fun setupRecyclerView(view: View, transactions: List<Transaction>) {
    val recyclerView: RecyclerView = view.findViewById(R.id.transactions_list)
    recyclerView.layoutManager = LinearLayoutManager(view.context)
    recyclerView.adapter = TransactionAdapter(transactions)
}


class BudgetHistoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_budget_history, container, false)



        val uuid = UUID.randomUUID()
        TransactionApiClient.transactionAPIService.getBuUserUID(uuid).enqueue(object : Callback<List<Transaction>> {
            override fun onResponse(call: Call<List<Transaction>>, response: Response<List<Transaction>>) {
                if (response.isSuccessful) {
                    // Получаем список транзакций
                    val transactions = response.body() ?: emptyList()

                    // Здесь вы можете передать этот список в RecyclerView через адаптер
                    setupRecyclerView(view, transactions)
                } else {
                    println()
                }
            }

            override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
//                toast("Ошибка сети: ${t.message}")
            }
        })


        return view
    }

}