package com.example.financialwunderwaffe.main.briefcase.states

data class AssetInformationState(
    val assetId: Long,
    val title: String,
    val date: String,
    val amount: String,
    val percentage: String?
)
