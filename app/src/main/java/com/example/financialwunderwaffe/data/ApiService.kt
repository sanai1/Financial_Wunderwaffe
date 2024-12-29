package com.example.financialwunderwaffe.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Header

interface ApiService {

    @GET("user/login")
    fun findByLogin(
        @Header("Authorization") token: String,
        @Query("login") login: String
    ): Call<String>

    @POST("user/register")
    fun registerUser(
        @Header("Authorization") token: String,
        @Body user: User
    ): Call<ResponseModel>

    @DELETE("user/delete")
    fun deleteUser(
        @Query("uid") uid: String
    ): Call<ResponseModel>

    @PUT("user/update")
    fun updateUser(
        @Body user: User
    ): Call<ResponseModel>
}

data class ResponseModel(
    val message: String
)