package com.example.financialwunderwaffe.retrofit.request

data class RequestTemplate<T>(
    val status: Status,
    val data: T?
)