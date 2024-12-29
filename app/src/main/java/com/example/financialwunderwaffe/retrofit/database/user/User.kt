package com.example.financialwunderwaffe.retrofit.database.user

data class User(
    val uid: String,
    val login: String,
    val password: String,
    val authority: String = "USER"
)