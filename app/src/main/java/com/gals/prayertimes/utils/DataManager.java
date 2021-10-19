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
    Intent toMain;
    Context activity;
    Prayer prayer;
    Boolean updateDate;
    ToolsManager tools;
    AppDB db;

    public DataManager(Intent intent, Prayer prayer, SharedPreferences settings, Context activity, Boolean updateData) {
        setToMain(intent);
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
            Log.i("DataManager/Info:", getTodayDate());
            if (tools.isNetworkAvailable()) {
                Prayer prayer = this.getPrayerFromServer(this.getTodayDate());
                if (prayer.isValid()) {
                    this.saveData(prayer);
                    prayer.printTest();
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
        db.prayerDao().insert(prayer);
    }

    protected Prayer getPrayerFromServer(String todayDate) {
        Prayer prayer = new Prayer();
        try {
            URL url = new URL("http://prayersapi.scienceontheweb.net/api/prayer/" + todayDate);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            InputStream inputStream = httpConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String htmlResponse = bufferedReader.readLine();
            httpConn.disconnect();

            Log.e("error", htmlResponse);
            JSONArray json = new JSONArray(htmlResponse);
            prayer.setObjectId(json.getJSONObject(0).getString("id"));
            prayer.setCreatedAt(null);
            prayer.setUpdatedAt(null);
            prayer.setSDate(json.getJSONObject(0).getString("sDate"));
            prayer.setMDate(json.getJSONObject(0).getString("mDate"));
            prayer.setFajer(json.getJSONObject(0).getString("fajer"));
            prayer.setSunrise(json.getJSONObject(0).getString("sunrise"));
            prayer.setDuhr(json.getJSONObject(0).getString("duhr"));
            prayer.setAsr(json.getJSONObject(0).getString("asr"));
            prayer.setMaghrib(json.getJSONObject(0).getString("maghrib"));
            prayer.setIsha(json.getJSONObject(0).getString("isha"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return prayer;
    }
}
