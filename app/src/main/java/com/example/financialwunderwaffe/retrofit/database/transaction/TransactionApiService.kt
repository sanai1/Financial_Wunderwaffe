package com.example.financialwunderwaffe.retrofit.database.transaction

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.util.UUID

interface TransactionApiService {

    @GET("transaction")
    fun getBuUserUID(
        @Query("userUID") userUID: UUID
    ): Call<List<Transaction>>

    @POST("transaction")
    fun createTransaction(
        @Body transaction: Transaction
    ): Call<Long>

    @PUT("transaction")
    fun updateTransactionByID(
        @Body transaction: Transaction
    ): Call<Boolean>

    @DELETE("transaction")
    fun deleteTransactionByID(
        @Query("transactionID") transactionID: Long
    ): Call<Boolean>

}