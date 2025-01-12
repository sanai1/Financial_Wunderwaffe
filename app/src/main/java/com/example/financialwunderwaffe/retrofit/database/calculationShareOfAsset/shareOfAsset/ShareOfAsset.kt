package com.example.financialwunderwaffe.retrofit.database.calculationShareOfAsset.shareOfAsset

import com.example.financialwunderwaffe.retrofit.database.calculationShareOfAsset.typeAsset.TypeAsset

data class ShareOfAsset(
    val id: Long,
    val typeAsset: TypeAsset,
    val share: Long
)