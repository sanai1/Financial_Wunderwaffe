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
    private val _dateBudget = MutableLiveData(
        Year.parse(
            LocalDate.now().year.toString(),
            DateTimeFormatter.ofPattern("yyyy")
        )
    )
    val dateBudget: LiveData<Year> = _dateBudget
    private val _large = MutableLiveData(Pair(false, false))
    val large: LiveData<Pair<Boolean, Boolean>> = _large
    private val _categories = MutableLiveData<MutableMap<Long, Boolean>>(mutableMapOf())
    val categories: LiveData<MutableMap<Long, Boolean>> = _categories

    fun setTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun setDateBudget(newDate: Year) {
        _dateBudget.value = newDate
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
    private val _dateAsset = MutableLiveData(
        Year.parse(
            LocalDate.now().year.toString(),
            DateTimeFormatter.ofPattern("yyyy")
        )
    )
    val dateAsset: LiveData<Year> = _dateAsset
    private val _assets = MutableLiveData<MutableMap<Long, Boolean>>()
    val assets: LiveData<MutableMap<Long, Boolean>> = _assets

    fun setDateAsset(newDate: Year) {
        _dateAsset.value = newDate
    }

    fun setAssets(newAssets: MutableMap<Long, Boolean>) {
        _assets.postValue(newAssets)
    }

    fun updateAsset(assetId: Long) {
        val newAssets = _assets.value!!.let {
            it[assetId] = it[assetId]!!.not()
            it
        }
        _assets.value = newAssets
    }

    private val _dateCapital = MutableLiveData(
        Year.parse(
            LocalDate.now().year.toString(),
            DateTimeFormatter.ofPattern("yyyy")
        )
    )

    val dateCapital: LiveData<Year> = _dateCapital

    fun setDateCapital(newDate: Year) {
        _dateCapital.value = newDate
    }

    fun updateCapitalByMonth(token: String, uid: UUID) = CoroutineScope(Dispatchers.IO).launch {
        val response =
            AnalyticsApiClient.analyticsAPIService.getCapitalByMonth(token, uid).execute()
        if (response.isSuccessful && response.body() != null) {
            _capitalByMonth.postValue(response.body())
        }
    }
}