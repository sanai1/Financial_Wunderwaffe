package com.example.financialwunderwaffe.main.budget.fragments.history

data class TransactionState(
    val id: Long,
    val categoryTitle: String,
    val amount: String,
    val date: String,
    val type: Boolean,
    val description: String
)
