package com.gals.prayertimes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Genius on 2/8/2018.
 */

public class GetServerData extends AsyncTask<String, Void, String> {
    String todayDate;
    Prayer prayer;
    SharedPreferences settings;
    Intent toMain;
    Context activity;
    Boolean updateDate;
    ToolsManager tools;

    public GetServerData(Intent intent, Prayer prayer, SharedPreferences settings, Context activity, Boolean updateData) {
        setToMain(intent);
        setPrayer(prayer);
        setSettings(settings);
        setActivity(activity);
        setUpdateDate(updateData);
        tools = new ToolsManager(getActivity());
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
                if (getPrayer().getTodayPrayers(getTodayDate())) {
                    getPrayer().setLocalStorage(getSettings());
                    getPrayer().printTest();
                } else {
                    getToMain().putExtra("result", getActivity().getString(R.string.checkInternet));
                }
            } else {
                getToMain().putExtra("checkInternt", getActivity().getString(R.string.checkInternet));
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
}
