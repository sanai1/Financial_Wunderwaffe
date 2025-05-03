package com.example.financialwunderwaffe.retrofit.database.asset.information.model

data class AssetTransaction(
    val id: Long = 0L,
    val assetID: Long,
    val date: String,
    val isSale: Boolean,
    val amount: Long
)
