package com.example.financialwunderwaffe.main.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.financialwunderwaffe.retrofit.database.category.Category
import com.example.financialwunderwaffe.retrofit.database.category.CategoryApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class BudgetViewModel : ViewModel() {
    private lateinit var update: () -> Unit
    fun setUpdate(newUpdate: () -> Unit) {
        update = newUpdate
    }

    private val _categories = MutableLiveData<List<Category>>(emptyList())
    val categories: LiveData<List<Category>> = _categories

    fun updateCategory(token: String, uid: UUID) = CoroutineScope(Dispatchers.IO).launch {
        val response = CategoryApiClient.categoryAPIService.getByUserUID(token, uid).execute()
        if (response.isSuccessful && response.body() != null) {
            _categories.postValue(response.body())
        }
    }
}