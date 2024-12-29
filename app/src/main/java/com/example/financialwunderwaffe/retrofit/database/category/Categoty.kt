package com.example.financialwunderwaffe.retrofit.database.category

import java.util.UUID

data class Categoty(
    val id: Long,
    val name: String,
    val type: Boolean,
    val user_uid: UUID
)
