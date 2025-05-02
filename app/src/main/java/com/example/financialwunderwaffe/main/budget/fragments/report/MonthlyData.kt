package com.example.financialwunderwaffe.main.budget.fragments.report

import java.time.YearMonth

data class MonthlyData(
    val date: YearMonth,
    val expense: Long,
    val income: Long
)
