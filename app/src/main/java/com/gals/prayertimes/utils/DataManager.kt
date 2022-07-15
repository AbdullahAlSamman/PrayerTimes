package com.gals.prayertimes.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import com.gals.prayertimes.EntityPrayer
import com.gals.prayertimes.R
import com.gals.prayertimes.db.AppDB
import com.gals.prayertimes.db.AppDB.Companion.getInstance
import com.gals.prayertimes.db.entities.Prayer.Companion.isValid
import com.gals.prayertimes.db.entities.Settings
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class DataManager(
    intent: Intent?,
    activity: Context?,
    updateData: Boolean
) : AsyncTask<String?, Void?, String?>() {
    @SuppressLint("StaticFieldLeak")
    private val activity: Context? = activity!!
    private lateinit var toMain: Intent
    private lateinit var todayDate: String
    private val tools: UtilsManager = UtilsManager(activity!!)
    private val db: AppDB = getInstance(activity!!)

    init {
        if (intent != null) {
            this.toMain = intent
        }
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: String?): String? {
        try {
            todayDate = SimpleDateFormat(
                "dd.MM.yyyy",
                Locale.US
            ).format(Date())
            if (tools.isRTL()) {
                todayDate = tools.convertDate()
            }
            Log.i(
                "DataManager/Info:",
                todayDate
            )
            if (tools.isNetworkAvailable()) {
              /**Done*/
            }
            if (!db.settingsDao.isExists) {
                saveSettings(
                    Settings(
                        false,
                        "silent"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun savePrayer(prayer: EntityPrayer?) {
        try {
            db.prayerDao.insert(prayer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveSettings(settings: Settings?) {
        try {
            db.settingsDao.insert(settings)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPrayerFromServer(todayDate: String?): EntityPrayer {
        var prayer: EntityPrayer = EntityPrayer.EMPTY
        try {
            val url = URL(
                activity?.getString(R.string.app_server_link) + todayDate
            )
            val httpConn = url.openConnection() as HttpURLConnection
            httpConn.requestMethod = "GET"
            val inputStream = httpConn.inputStream
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val htmlResponse = bufferedReader.readLine()
            httpConn.disconnect()
            Log.e(
                "Server Data",
                htmlResponse
            )
            val json = JSONArray(htmlResponse)
            prayer = EntityPrayer(
                objectId = json.getJSONObject(0).getString("id"),
                sDate = json.getJSONObject(0).getString("sDate"),
                mDate = json.getJSONObject(0).getString("mDate"),
                fajer = json.getJSONObject(0).getString("fajer"),
                sunrise = json.getJSONObject(0).getString("sunrise"),
                duhr = json.getJSONObject(0).getString("duhr"),
                asr = json.getJSONObject(0).getString("asr"),
                maghrib = json.getJSONObject(0).getString("maghrib"),
                isha = json.getJSONObject(0).getString("isha"),
            )
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return prayer
    }
}
