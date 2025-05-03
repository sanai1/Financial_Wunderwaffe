package com.example.financialwunderwaffe.retrofit.database.asset.information.model

data class AssetInformation(
    val assetId: Long,
    val typeInformation: String,
    val date: String,
    val amount: Long?,
    val isSale: Boolean?,
    val oldPrice: Long?,
    val currentPrice: Long?
)
