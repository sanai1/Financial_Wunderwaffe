package com.example.financialwunderwaffe.main.briefcase

data class AssetInformationState(
    val assetId: Long,
    val title: String,
    val date: String,
    val amount: String,
    val percentage: String?
)
