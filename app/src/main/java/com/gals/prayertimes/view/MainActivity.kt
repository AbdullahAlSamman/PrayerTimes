package com.gals.prayertimes.view

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.DomainPrayer
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.R
import com.gals.prayertimes.databinding.ActivityMainBinding
import com.gals.prayertimes.db.AppDB
import com.gals.prayertimes.db.AppDB.Companion.getInstance
import com.gals.prayertimes.db.entities.Prayer.Companion.toDomain
import com.gals.prayertimes.utils.DataManager
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.viewmodel.MainViewModel
import com.gals.prayertimes.viewmodel.MainViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var binding: ActivityMainBinding
    lateinit var db: AppDB
    lateinit var domainPrayer: DomainPrayer
    lateinit var prayer: EntityPrayer
    lateinit var tools: UtilsManager
    lateinit var btnSettings: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: Internet warning

        /**Tools and DB*/
        tools = UtilsManager(baseContext)
        db = getInstance(baseContext)

        /** View Model Factory and data binding*/
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding.lifecycleOwner = this

        viewModelFactory = MainViewModelFactory(
            application = this.applicationContext,
            database = db
        )
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MainViewModel::class.java]

        /**Change Status bar color*/
        tools.changeStatusBarColor(window)
        tools.setActivityLanguage("en")
        val settingsActivity = Intent(
            this,
            Menu::class.java
        )
        btnSettings = findViewById(R.id.settingButton)
        btnSettings.setOnClickListener { startActivity(settingsActivity) }
        GetPrayerFromDB().execute(baseContext)
    }

    override fun onResume() {
        super.onResume()
        DataManager(
            null,
            this.baseContext,
            true
        ).execute(
            "",
            "",
            ""
        )
    }

    private fun convertEntityToDomain() {
        domainPrayer = prayer.toDomain()
    }

    /**
     * Get data from db.
     */
    inner class GetPrayerFromDB : AsyncTask<Context?, String?, String?>() {
        override fun doInBackground(vararg params: Context?): String? {
            try {
                prayer = db.prayerDao()?.findByDate(
                    SimpleDateFormat(
                        "dd.MM.yyyy",
                        Locale.US
                    ).format(Date())
                )!!
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            convertEntityToDomain()
            viewModel.updateViewObservableValues(prayer.toDomain())
            viewModel.domainPrayer = prayer.toDomain()
            viewModel.startUIUpdate()
        }
    }
}
