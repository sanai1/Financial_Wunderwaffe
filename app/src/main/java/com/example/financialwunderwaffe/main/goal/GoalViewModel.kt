package com.example.financialwunderwaffe.main.goal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GoalViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Скоро тут появится кое-что интересное"
    }
    val text: LiveData<String> = _text
}