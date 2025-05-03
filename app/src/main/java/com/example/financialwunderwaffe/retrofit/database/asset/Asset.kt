package com.example.financialwunderwaffe.retrofit.database.asset

import java.util.UUID

data class Asset(
    val id: Long = 0L,
    val userUID: UUID,
    val title: String,
    val amount: Long = 0L
)
