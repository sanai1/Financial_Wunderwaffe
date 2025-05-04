package com.example.financialwunderwaffe.retrofit.database.analytics.model

data class CapitalAnalytics(
    val month: String,
    val saveRate: Double,
    val listAsset: List<AssetAnalytics>
) {
    data class AssetAnalytics(
        val id: Long,
        val title: String,
        val amount: Long
    )
}
