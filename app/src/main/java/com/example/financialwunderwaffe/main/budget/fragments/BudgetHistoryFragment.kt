package com.example.financialwunderwaffe.main.budget.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    println("Передалось ${transactions.size} транзакций")
    val recyclerView: RecyclerView = view.findViewById(R.id.transactions_list)
    recyclerView.layoutManager = LinearLayoutManager(view.context)
    recyclerView.adapter = TransactionAdapter(transactions)
}


class BudgetHistoryFragment : Fragment() {
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_budget_history, container, false)

        transactionAdapter = TransactionAdapter(emptyList())
        val recyclerView: RecyclerView = view.findViewById(R.id.transactions_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = transactionAdapter

        fetchTransactions(view)

        return view
    }

    private fun fetchTransactions(view: View) {
        val s: String = "7852057a-ecf6-40ba-b3eb-aad3d6be818f"
        val uuid = UUID.fromString(s)
        val email = "siuuu"
        val password = "123456"
        val base = "Basic " + Base64.getEncoder().encodeToString("$email:$password".toByteArray())

        TransactionApiClient.transactionAPIService.getBuUserUID(base, uuid)
            .enqueue(object : Callback<List<Transaction>> {
                override fun onResponse(
                    call: Call<List<Transaction>>, response: Response<List<Transaction>>
                ) {
                    if (response.isSuccessful) {
                        val transactions = response.body() ?: emptyList()
                        println("Получилось ${transactions.size} транзакций")
                        if (transactions.isEmpty()) {
                            println("Список транзакций пуст")
                        }
                        transactionAdapter = TransactionAdapter(transactions)
                        val recyclerView: RecyclerView =
                            view.findViewById(R.id.transactions_list)
                        recyclerView.adapter = transactionAdapter
                    } else {
                        println("Ошибка сервера: Код ${response.code()}, Сообщение: ${response.message()}")
                        println("Тело ошибки: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                    println("Ошибка сети: ${t.message}")
                }
            })
    }
}