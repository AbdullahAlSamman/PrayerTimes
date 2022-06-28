package com.gals.prayertimes.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.db.AppDB

class MainViewModelFactory(
    private val application: Context,
    private val database: AppDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                application = this.application,
                database = this.database
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}