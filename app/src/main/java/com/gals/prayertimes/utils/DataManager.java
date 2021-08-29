package com.gals.prayertimes.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.gals.prayertimes.R;
import com.gals.prayertimes.db.AppDB;
import com.gals.prayertimes.model.Prayer;
import com.gals.prayertimes.utils.ToolsManager;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Genius on 2/8/2018.
 */

public class DataManager extends AsyncTask<String, Void, String> {
    String todayDate;
    Prayer prayer;
    SharedPreferences settings;
    Intent toMain;
    Context activity;
    Boolean updateDate;
    ToolsManager tools;
    AppDB db;

    public DataManager(Intent intent, Prayer prayer, SharedPreferences settings, Context activity, Boolean updateData) {
        setToMain(intent);
        setPrayer(prayer);
        setSettings(settings);
        setActivity(activity);
        setUpdateDate(updateData);
        tools = new ToolsManager(getActivity());
        db = AppDB.getInstance(getActivity());
    }

    public Boolean getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Boolean updateDate) {
        this.updateDate = updateDate;
    }

    public Context getActivity() {
        return activity;
    }

    public void setActivity(Context activity) {
        this.activity = activity;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public Prayer getPrayer() {
        return prayer;
    }

    public void setPrayer(Prayer prayer) {
        this.prayer = prayer;
    }

    public SharedPreferences getSettings() {
        return settings;
    }

    public void setSettings(SharedPreferences settings) {
        this.settings = settings;
    }

    public Intent getToMain() {
        return toMain;
    }

    public void setToMain(Intent toMain) {
        this.toMain = toMain;
    }

    @Override
    protected String doInBackground(String... strings) {
        // here fix arabic numbers
        try {
            setTodayDate(new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
            if (tools.isRTL()) {
                setTodayDate(tools.convertDate());
            }
            Log.i("Info:", getTodayDate());
            if (tools.isNetworkAvailable()) {
                if (this.getPrayerFromServer(this.getTodayDate())) {
                    this.saveData(getPrayer());
                    getPrayer().printTest();
                } else {
                    getToMain().putExtra("result", getActivity().getString(R.string.checkInternet));
                }
            } else {
                getToMain().putExtra("checkInternet", getActivity().getString(R.string.checkInternet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //change the course of events for update or splashscreen
        if (!getUpdateDate()) {
            getToMain().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(getToMain());
        }
    }

    protected void saveData(Prayer prayer) {
        db.prayerDao().insert(getPrayer());
    }

    protected boolean getPrayerFromServer(String todayDate) {
        boolean result = false;
        try {
            URL url = new URL("http://prayers.esy.es/api/prayers/" + todayDate);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            InputStream inputStream = httpConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            httpConn.disconnect();

            JSONArray json = new JSONArray(line);
            getPrayer().setObjectId(json.getJSONObject(0).getString("id"));
            getPrayer().setCreatedAt(null);
            getPrayer().setUpdatedAt(null);
            getPrayer().setSDate(json.getJSONObject(0).getString("sDate"));
            getPrayer().setMDate(json.getJSONObject(0).getString("mDate"));
            getPrayer().setFajer(json.getJSONObject(0).getString("fajer"));
            getPrayer().setSunrise(json.getJSONObject(0).getString("sunrise"));
            getPrayer().setDuhr(json.getJSONObject(0).getString("duhr"));
            getPrayer().setAsr(json.getJSONObject(0).getString("asr"));
            getPrayer().setMaghrib(json.getJSONObject(0).getString("maghrib"));
            getPrayer().setIsha(json.getJSONObject(0).getString("isha"));
            result = true;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result;
    }
}
