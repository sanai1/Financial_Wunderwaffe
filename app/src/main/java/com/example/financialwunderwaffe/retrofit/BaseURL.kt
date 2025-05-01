package com.example.financialwunderwaffe.retrofit

object BaseURL {

    private var BASE_URL: String? = null

    init {
        BASE_URL = "http://176.98.178.254:8080/api/v1/"
    }

    fun getURL(): String = BASE_URL!!

}