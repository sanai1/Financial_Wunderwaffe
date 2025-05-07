package com.example.financialwunderwaffe.main.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.financialwunderwaffe.retrofit.database.category.Category
import com.example.financialwunderwaffe.retrofit.database.category.CategoryApiClient
import com.example.financialwunderwaffe.retrofit.database.transaction.Transaction
import com.example.financialwunderwaffe.retrofit.database.transaction.TransactionApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.YearMonth
import java.util.UUID

class BudgetViewModel : ViewModel() {
    private lateinit var update: () -> Unit
    private lateinit var toast: (String) -> Unit
    fun setUpdate(newUpdate: () -> Unit) {
        update = newUpdate
    }
    fun setToast(newToast: (String) -> Unit) {
        toast = newToast
    }

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories
    private val _typeCategory = MutableLiveData<Boolean>(false)
    val typeCategory: LiveData<Boolean> = _typeCategory

    fun setTypeCategory(newTypeCategory: Boolean) {
        _typeCategory.value = newTypeCategory
    }

    fun updateCategory(token: String, uid: UUID) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = CategoryApiClient.categoryAPIService.getByUserUID(token, uid).execute()
            if (response.isSuccessful && response.body() != null) {
                _categories.postValue(response.body())
            } else withContext(Dispatchers.Main) {
                toast("Ошибка сервера: ${response.code()}")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                toast("Ошибка сети: ${e.message}")
            }
        }
    }

    fun createCategory(token: String, categoryView: Category) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    CategoryApiClient.categoryAPIService.createCategory(token, categoryView)
                        .execute()
                if (response.isSuccessful && (response.body() ?: 0L) > 0L) {
                    update()
                } else withContext(Dispatchers.Main) {
                    toast("Ошибка сервера: ${response.message()}")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    toast("Ошибка сети: ${e.message}")
                }
            }
        }

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transaction: LiveData<List<Transaction>> = _transactions
    private val _dateHistory = MutableLiveData(YearMonth.now())
    val dateHistory: LiveData<YearMonth> = _dateHistory
    private val _typeTransaction = MutableLiveData<Boolean?>()
    val typeTransaction: LiveData<Boolean?> = _typeTransaction

    fun setTypeTransaction(newTypeTransaction: Boolean?) {
        _typeTransaction.value = newTypeTransaction
    }

    fun setDateHistory(newDateHistory: YearMonth) {
        _dateHistory.value = newDateHistory
    }

    fun updateTransaction(token: String, uid: UUID) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response =
                TransactionApiClient.transactionAPIService.getBuUserUID(token, uid).execute()
            if (response.isSuccessful && response.body() != null) {
                _transactions.postValue(response.body())
            } else withContext(Dispatchers.Main) {
                toast("Ошибка сервера: ${response.message()}")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                toast("Ошибка сети: ${e.message}")
            }
        }
    }

    fun createTransaction(token: String, transactionView: Transaction) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    TransactionApiClient.transactionAPIService.createTransaction(
                        token,
                        transactionView
                    )
                        .execute()
                if (response.isSuccessful && (response.body() ?: 0L) > 0L) {
                    update()
                    withContext(Dispatchers.Main) {
                        toast("Транзакция добавлена")
                    }
                } else withContext(Dispatchers.Main) {
                    toast("Ошибка сервера: ${response.message()}")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    toast("Ошибка сети: ${e.message}")
                }
            }
        }

}