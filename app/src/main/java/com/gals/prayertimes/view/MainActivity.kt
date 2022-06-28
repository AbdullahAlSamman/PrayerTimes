package com.gals.prayertimes.view

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.DomainPrayer
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.R
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
    lateinit var db: AppDB
    lateinit var domainPrayer: DomainPrayer
    lateinit var prayer: EntityPrayer
    lateinit var tools: UtilsManager
    lateinit var fajerLay: LinearLayout
    lateinit var sunriseLay: LinearLayout
    lateinit var duhrLay: LinearLayout
    lateinit var asrLay: LinearLayout
    lateinit var maghribLay: LinearLayout
    lateinit var ishaLay: LinearLayout
    lateinit var fajerText: TextView
    lateinit var sunriseText: TextView
    lateinit var duhrText: TextView
    lateinit var asrText: TextView
    lateinit var maghribText: TextView
    lateinit var ishaText: TextView
    lateinit var sDateBanner: TextView
    lateinit var mDateBanner: TextView
    lateinit var dayBanner: TextView
    lateinit var fajerTime: TextView
    lateinit var sunriseTime: TextView
    lateinit var duhrTime: TextView
    lateinit var asrTime: TextView
    lateinit var maghribTime: TextView
    lateinit var ishaTime: TextView
    lateinit var nextPrayerTime: TextView
    lateinit var nextPrayerText: TextView
    lateinit var nextPrayerBanner: TextView
    lateinit var btnSettings: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: Internet warning

        /**Tools and DB*/
        tools = UtilsManager(baseContext)
        db = getInstance(baseContext)

        /** View Model Factory and data binding*/
        val binding: ViewDataBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
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
        viewModel.startUIUpdate()
        changeFontSizes()
        tools.setActivityLanguage("en")
        val settingsActivity = Intent(
            this,
            Menu::class.java
        )
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

    /**
     * Fonts for small devices and change the size of Squares
     */
    private fun changeFontSizes() {
        if (tools.isScreenSmall) {
            sDateBanner.textSize = 16f
            mDateBanner.textSize = 16f
            dayBanner.textSize = 16f
            nextPrayerBanner.textSize = 18f
            nextPrayerText.textSize = 18f
            nextPrayerTime.textSize = 20f
            fajerText.textSize = 16f
            sunriseText.textSize = 16f
            duhrText.textSize = 16f
            asrText.textSize = 16f
            maghribText.textSize = 16f
            ishaText.textSize = 16f
            fajerTime.textSize = 16f
            sunriseTime.textSize = 16f
            duhrTime.textSize = 16f
            asrTime.textSize = 16f
            maghribTime.textSize = 16f
            ishaTime.textSize = 16f
            val heightParams = fajerLay.layoutParams as LinearLayout.LayoutParams
            heightParams.height = 150
            fajerLay.layoutParams = heightParams
            sunriseLay.layoutParams = heightParams
            duhrLay.layoutParams = heightParams
            asrLay.layoutParams = heightParams
            maghribLay.layoutParams = heightParams
            ishaLay.layoutParams = heightParams
        }
    }

    private fun convertEntityToDomain() {
        domainPrayer = prayer.toDomain()
    }

    /**
     * Get data from db.
     */
    inner class GetPrayerFromDB : AsyncTask<Context?, String?, String?>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Context?): String? {
            try {
                prayer = db.prayerDao()?.findByDate(
                    SimpleDateFormat(
                        "dd.MM.yyyy",
                        Locale.US
                    ).format(Date())
                )!!
                convertEntityToDomain()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            if (viewModel.isValid) {
                viewModel.startUIUpdate()
            }
        }
    }
}
