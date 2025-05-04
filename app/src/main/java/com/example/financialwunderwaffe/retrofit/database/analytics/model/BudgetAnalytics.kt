package com.example.financialwunderwaffe.retrofit.database.analytics.model

data class BudgetAnalytics(
    val month: String,
    val listCategory: List<CategoryAnalytics>
) {
    data class CategoryAnalytics(
        val id: Long,
        val title: String,
        val isIncome: Boolean,
        val amountLarge: Long,
        val amountUsual: Long
    )
}
