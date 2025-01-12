package com.example.financialwunderwaffe.retrofit.database.calculationShareOfAsset.shareOfAsset

import java.util.UUID

data class CalculationShareOfAsset(
    val id: Long,
    val userUID: UUID,
    val date: String,
    val listShareOfAsset: List<ShareOfAsset>
)