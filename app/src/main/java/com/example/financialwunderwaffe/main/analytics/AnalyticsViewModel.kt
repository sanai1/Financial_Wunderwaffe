package com.example.financialwunderwaffe.main.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.financialwunderwaffe.retrofit.database.analytics.AnalyticsApiClient
import com.example.financialwunderwaffe.retrofit.database.analytics.model.BudgetAnalytics
import com.example.financialwunderwaffe.retrofit.database.analytics.model.CapitalAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.UUID

class AnalyticsViewModel : ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title
    private val _date = MutableLiveData(
        Year.parse(
            LocalDate.now().year.toString(),
            DateTimeFormatter.ofPattern("yyyy")
        )
    )
    val date: LiveData<Year> = _date
    private val _large = MutableLiveData(Pair(false, false))
    val large: LiveData<Pair<Boolean, Boolean>> = _large
    private val _categories = MutableLiveData<MutableMap<Long, Boolean>>(mutableMapOf())
    val categories: LiveData<MutableMap<Long, Boolean>> = _categories

    fun setTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun setDate(newDate: Year) {
        _date.value = newDate
    }

    fun setLarge(newLarge: Pair<Boolean, Boolean>) {
        _large.value = newLarge
    }

    fun setCategories(newCategories: MutableMap<Long, Boolean>) {
        _categories.postValue(newCategories)
    }

    fun updateCategories(categoryId: Long) {
        val newCategories = _categories.value!!.let {
            it[categoryId] = it[categoryId]!!.not()
            it
        }
        _categories.value = newCategories
    }

    private val _budgetByMonth = MutableLiveData<List<BudgetAnalytics>>(emptyList())
    val budgetByMonth: LiveData<List<BudgetAnalytics>> = _budgetByMonth

    fun updateBudgetByMonth(token: String, uid: UUID) = CoroutineScope(Dispatchers.IO).launch {
        val response = AnalyticsApiClient.analyticsAPIService.getBudgetByMonth(token, uid).execute()
        if (response.isSuccessful && response.body() != null) {
            _budgetByMonth.postValue(response.body())
        }
    }

    private val _capitalByMonth = MutableLiveData<List<CapitalAnalytics>>(emptyList())
    val capitalByMonth: LiveData<List<CapitalAnalytics>> = _capitalByMonth

    fun updateCapitalByMonth(token: String, uid: UUID) = CoroutineScope(Dispatchers.IO).launch {
        val response =
            AnalyticsApiClient.analyticsAPIService.getCapitalByMonth(token, uid).execute()
        if (response.isSuccessful && response.body() != null) {
            _capitalByMonth.postValue(response.body())
        }
    }
}