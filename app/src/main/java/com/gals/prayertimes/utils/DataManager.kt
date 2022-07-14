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
import java.util.Date
import java.util.Locale

class DataManager(
    intent: Intent?,
    activity: Context?,
    updateData: Boolean
) : AsyncTask<String?, Void?, String?>() {
    @SuppressLint("StaticFieldLeak")
    private var activity: Context? = null
    private lateinit var toMain: Intent
    private lateinit var todayDate: String
    private val updateDate: Boolean
    private val tools: UtilsManager
    private val db: AppDB

    init {
        if (intent != null) {
            this.toMain = intent
        }
        this.activity = activity
        this.updateDate = updateData
        this.tools = activity?.let { UtilsManager(it) }!!
        this.db = getInstance(activity)
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: String?): String? {
        try {
            todayDate = SimpleDateFormat(
                "dd.MM.yyyy",
                Locale.US
            ).format(Date())
            if (tools.isRTL) {
                todayDate = tools.convertDate()
            }
            Log.i(
                "DataManager/Info:",
                todayDate
            )
            if (tools.isNetworkAvailable) {
                val prayer = getPrayerFromServer(todayDate)
                if (prayer.isValid()) {
                    tools.printTest(prayer)
                    savePrayer(prayer)
                }
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

    override fun onPostExecute(s: String?) {
        super.onPostExecute(s)
        /**Change the course of events for update or splashscreen*/
        if (!updateDate) {
            toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            activity!!.startActivity(toMain)
        }
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
