package com.example.financialwunderwaffe.retrofit.database.user

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Header

interface UserApiService {

    @GET("user/login")
    fun findByLogin(
        @Header("Authorization") token: String,
        @Query("login") login: String
    ): Call<String>

    @POST("user/register")
    fun registerUser(
        @Body user: User
    ): Call<Boolean>

}