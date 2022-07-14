package com.gals.prayertimes.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gals.prayertimes.db.AppDB
import com.gals.prayertimes.utils.Repository
import com.gals.prayertimes.utils.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class SplashscreenViewModel(
    private val database: AppDB,
    private val repository: Repository
) : ViewModel() {
    val loading = MutableLiveData<Boolean>()

    fun getPrayer() {
        val todayDate = SimpleDateFormat(
            "dd.MM.yyyy",
            Locale.US
        ).format(Date())

        viewModelScope.launch {
            loading.postValue(true)
            val response = repository.getPrayer(todayDate)
            withContext(Dispatchers.IO) {
                if (response!!.isSuccessful) {
                    println(response.body()?.first().toString())
                    database.prayerDao.insert(response.body()!!.first().toEntity())
                } else {
                    Log.e("SplashVM Data Request", response.message())
                }
            }
            withContext(Dispatchers.Main){
                if(response!!.isSuccessful){
                    loading.value = false
                }
            }
        }
    }

}