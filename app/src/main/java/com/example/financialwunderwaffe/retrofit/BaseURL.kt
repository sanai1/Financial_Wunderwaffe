package com.example.financialwunderwaffe.retrofit

object BaseURL {

    private var BASE_URL: String? = null

    init {
        BASE_URL = "http://37.252.20.109:8080/api/v1/"
    }

    fun getURL(): String = BASE_URL!!

}