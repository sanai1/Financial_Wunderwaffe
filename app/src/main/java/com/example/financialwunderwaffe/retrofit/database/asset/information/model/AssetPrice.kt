package com.example.financialwunderwaffe.retrofit.database.asset.information.model

data class AssetPrice(
    val id: Long = 0L,
    val assetID: Long,
    val date: String,
    val oldPrice: Long,
    val currentPrice: Long
)
