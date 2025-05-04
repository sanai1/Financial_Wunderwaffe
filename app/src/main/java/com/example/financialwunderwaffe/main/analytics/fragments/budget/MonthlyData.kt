package com.example.financialwunderwaffe.main.analytics.fragments.budget

import java.time.YearMonth

data class MonthlyData(
    val date: YearMonth,
    val expense: Long,
    val income: Long
)
