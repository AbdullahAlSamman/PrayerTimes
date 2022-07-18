package com.gals.prayertimes.view

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.databinding.ActivityMainBinding
import com.gals.prayertimes.repository.db.AppDB
import com.gals.prayertimes.repository.db.AppDB.Companion.getInstance
import com.gals.prayertimes.repository.db.entities.Prayer.Companion.isValid
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.utils.toDomain
import com.gals.prayertimes.viewmodel.MainViewModel
import com.gals.prayertimes.viewmodel.factory.MainViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var binding: ActivityMainBinding
    lateinit var db: AppDB
    lateinit var tools: UtilsManager
    var prayer: EntityPrayer = EntityPrayer.EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: Internet warning
        configureTools()
        configureDataBinding()
        configureMVVM()
        configureUI()

        GetPrayerFromDB().execute(baseContext)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateDateUIObservables()
    }

    /**
     * Get data from db.
     */
    inner class GetPrayerFromDB : AsyncTask<Context?, String?, String?>() {
        override fun doInBackground(vararg params: Context?): String? {
            try {
                prayer = db.prayerDao.findByDate(
                    SimpleDateFormat(
                        "dd.MM.yyyy",
                        Locale.US
                    ).format(Date())
                )!!
                if (prayer.isValid().not()) {
                    prayer = EntityPrayer.EMPTY
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            viewModel.updateViewObservableValues(prayer.toDomain())
            viewModel.startDateUpdate()
        }
    }

    private fun configureMVVM() {
        viewModelFactory = MainViewModelFactory(
            application = this.applicationContext,
            database = db
        )
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MainViewModel::class.java]

        binding.viewModel = viewModel
    }

    private fun configureDataBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
    }

    private fun configureUI() {
        tools.changeStatusBarColor(window)
        tools.setActivityLanguage("en")
    }

    private fun configureTools() {
        tools = UtilsManager(baseContext)
        db = getInstance(baseContext)
    }
}
