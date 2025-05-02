package com.example.financialwunderwaffe.retrofit.database.transaction

import java.util.UUID

data class Transaction(
    val id: Long = 0L,
    val categoryID: Long,
    val amount: Long,
    val date: String,
    val type: Boolean,
    val description: String,
    val userUID: UUID
)