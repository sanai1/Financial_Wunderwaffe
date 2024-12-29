package com.example.financialwunderwaffe.data

data class User(
    val uid: String,
    val login: String,
    val password: String,
    val authority: String = "USER"
)