package com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments.main

data class SharesOfAssetsState(
    val typeAssets: String,
    val now: Long,
    val goal: Long,
    val delta: Long,
    val color: List<Int> = listOf(0, 0, 0)
)