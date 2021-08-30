package com.gals.prayertimes.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.gals.prayertimes.R;
import com.gals.prayertimes.utils.ToolsManager;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Genius on 1/26/2018.
 */

@Entity(tableName = "prayers")
public class Prayer {
    @Ignore
    private static final String parseClassName = "Prayers";
    @Ignore
    ToolsManager tools;
    @Ignore
    Uri fullAthan = Uri.parse("android.resource://com.gals.prayertimes/" + R.raw.fullathan);
    @Ignore
    Uri halfAthan = Uri.parse("android.resource://com.gals.prayertimes/" + R.raw.halfathan);
    @Ignore
    Uri silentAthan = Uri.parse("android.resource://com.gals.prayertimes/" + R.raw.silent);
    @Ignore
    private String currentPrayerName;
    @Ignore
    private String remmainingPrayerTime;
    @Ignore
    private Context activity;
    @PrimaryKey
    @NonNull
    private String objectId;
    @Ignore
    private Date updatedAt;
    @Ignore
    private Date createdAt;
    @Ignore
    private String sFullDate;
    @Ignore
    private String mFullDate;
    @Ignore
    private String day;
    private String sDate;
    private String mDate;
    private String fajer;
    private String sunrise;
    private String duhr;
    private String asr;
    private String maghrib;
    private String isha;
    @Ignore
    private SharedPreferences settings;
    @Ignore
    private Boolean notificaiton;
    @Ignore
    private String notificaitonType;
    @Ignore
    private Calendar fajerTime;
    @Ignore
    private Calendar sunriseTime;
    @Ignore
    private Calendar duhrTime;
    @Ignore
    private Calendar asrTime;
    @Ignore
    private Calendar sunsetTime;
    @Ignore
    private Calendar ishaTime;
    @Ignore
    private Calendar midNightTime;
    @Ignore
    private Calendar currentTime;
    @Ignore
    private String[] ssTime;
    @Ignore
    private String[] frTime;
    @Ignore
    private String[] srTime;
    @Ignore
    private String[] duTime;
    @Ignore
    private String[] asTime;
    @Ignore
    private String[] isTime;
    @Ignore
    private String[] cTime;

    public Prayer(Context activity) {
        setActivity(activity);
        tools = new ToolsManager(getActivity());
//        if (this.getLocalStorage()) {
//            Log.i("Prayer/Constractor", "Loaded from the Localstorage");
//        } else {
//            Log.i("Prayer/Constructor", "There is no date in Localstorage");
//        }

        // To Convert Strings to Time using Calender instance
        setCalenderPrayersTime();
    }

    public Prayer() {

    }

    public Boolean getNotificaiton() {
        return notificaiton;
    }

    public void setNotificaiton(Boolean notificaiton) {
        this.notificaiton = notificaiton;
    }

    public String getNotificaitonType() {
        return notificaitonType;
    }

    public void setNotificaitonType(String notificaitonType) {
        this.notificaitonType = notificaitonType;
    }

    public void setCalenderPrayersTime() {
        fajerTime = Calendar.getInstance();
        sunriseTime = Calendar.getInstance();
        duhrTime = Calendar.getInstance();
        asrTime = Calendar.getInstance();
        sunsetTime = Calendar.getInstance();
        ishaTime = Calendar.getInstance();
        midNightTime = Calendar.getInstance();

        ssTime = getMaghrib().split(":");
        frTime = getFajer().split(":");
        srTime = getSunrise().split(":");
        duTime = getDuhr().split(":");
        asTime = getAsr().split(":");
        isTime = getIsha().split(":");

        fajerTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(frTime[0].trim()));
        fajerTime.set(Calendar.MINUTE, Integer.parseInt(frTime[1].trim()));

        sunriseTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(srTime[0].trim()));
        sunriseTime.set(Calendar.MINUTE, Integer.parseInt(srTime[1].trim()));

        duhrTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(duTime[0].trim()));
        duhrTime.set(Calendar.MINUTE, Integer.parseInt(duTime[1].trim()));

        asrTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(asTime[0].trim()));
        asrTime.set(Calendar.MINUTE, Integer.parseInt(asTime[1].trim()));

        sunsetTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ssTime[0].trim()));
        sunsetTime.set(Calendar.MINUTE, Integer.parseInt(ssTime[1].trim()));

        ishaTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(isTime[0].trim()));
        ishaTime.set(Calendar.MINUTE, Integer.parseInt(isTime[1].trim()));

        midNightTime.set(Calendar.HOUR_OF_DAY, 23);
        midNightTime.set(Calendar.MINUTE, 59);
    }

    public String getSFullDate() {
        return sFullDate;
    }

    public void setSFullDate(String sFullDate) {
        this.sFullDate = sFullDate;
    }

    public String getMFullDate() {
        return mFullDate;
    }

    public void setMFullDate(String mFullDate) {
        this.mFullDate = mFullDate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCurrentPrayerName() {
        return currentPrayerName;
    }

    public void setCurrentPrayerName(String currentPrayerName) {
        this.currentPrayerName = currentPrayerName;
    }

    public String getRemmainingPrayerTime() {
        return remmainingPrayerTime;
    }

    public void setRemmainingPrayerTime(String remmainingPrayerTime) {
        this.remmainingPrayerTime = remmainingPrayerTime;
    }

    public SharedPreferences getSettings() {
        return settings;
    }

    public void setSettings(SharedPreferences settings) {
        this.settings = settings;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getSDate() {
        return sDate;
    }

    public void setSDate(String sDate) {
        this.sDate = sDate;
    }

    public String getMDate() {
        return mDate;
    }

    public void setMDate(String mDate) {
        this.mDate = mDate;
    }

    public String getFajer() {
        return fajer;
    }

    public void setFajer(String fajer) {
        this.fajer = fajer;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getDuhr() {
        return duhr;
    }

    public void setDuhr(String duhr) {
        this.duhr = duhr;
    }

    public String getAsr() {
        return asr;
    }

    public void setAsr(String asr) {
        this.asr = asr;
    }

    public String getMaghrib() {
        return maghrib;
    }

    public void setMaghrib(String maghrib) {
        this.maghrib = maghrib;
    }

    public String getIsha() {
        return isha;
    }

    public void setIsha(String isha) {
        this.isha = isha;
    }

    public Context getActivity() {
        return activity;
    }

    public void setActivity(Context activity) {
        this.activity = activity;
    }

    public Boolean getTodayPrayers(String dateToday) {
        Boolean result = false;
        try {
            URL url = new URL("http://prayers.esy.es/api/prayers/" + dateToday);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            InputStream inputStream = httpConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            httpConn.disconnect();
            JSONArray json = new JSONArray(line);

            setObjectId(json.getJSONObject(0).getString("id"));
            setCreatedAt(null);
            setUpdatedAt(null);
            setSDate(json.getJSONObject(0).getString("sDate"));
            setMDate(json.getJSONObject(0).getString("mDate"));
            setFajer(json.getJSONObject(0).getString("fajer"));
            setSunrise(json.getJSONObject(0).getString("sunrise"));
            setDuhr(json.getJSONObject(0).getString("duhr"));
            setAsr(json.getJSONObject(0).getString("asr"));
            setMaghrib(json.getJSONObject(0).getString("maghrib"));
            setIsha(json.getJSONObject(0).getString("isha"));
            result = true;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result;
    }

    public Boolean getLocalStorage() {
        if (getSettings().getString("objectId", "") != null) {
            setObjectId(getSettings().getString("objectId", null));
            setSDate(getSettings().getString("sDate", ""));
            setMDate(getSettings().getString("mDate", ""));
            setFajer(getSettings().getString("fajer", "4:00"));
            setSunrise(getSettings().getString("sunrise", "8:00"));
            setDuhr(getSettings().getString("duhr", "12:00"));
            setAsr(getSettings().getString("asr", "16:00"));
            setMaghrib(getSettings().getString("maghrib", "18:00"));
            setIsha(getSettings().getString("isha", "21:00"));
            setNotificaiton(getSettings().getBoolean("notification", true));
            setNotificaitonType(getSettings().getString("notificationType", "tone")); // Values: full, half, tone, silent
            return true;
        }
        return false;
    }

    public Boolean setLocalStorage(SharedPreferences settings) {
        try {
            SharedPreferences.Editor editor = settings.edit();
            if (getObjectId() != null && getCreatedAt() != null && getUpdatedAt() != null) {
                editor.putString("objectId", getObjectId().toString());
                editor.putString("createdAt", getCreatedAt().toString());
                editor.putString("updatedAt", getUpdatedAt().toString());
            }
            editor.putString("sDate", getSDate());
            editor.putString("mDate", getMDate());
            editor.putString("fajer", getFajer());
            editor.putString("sunrise", getSunrise());
            editor.putString("duhr", getDuhr());
            editor.putString("asr", getAsr());
            editor.putString("maghrib", getMaghrib());
            editor.putString("isha", getIsha());
            editor.putBoolean("notification", getNotificaiton());
            editor.putString("notificationType", getNotificaitonType());

            editor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void printTest() {
        Log.i("Prayer/info", getObjectId() + "\n" +
                getCreatedAt() + "\n" +
                getUpdatedAt() + "\n" +
                getSDate() + "\n" +
                getMDate() + "\n" +
                getFajer() + "\n" +
                getSunrise() + "\n" +
                getDuhr() + "\n" +
                getAsr() + "\n" +
                getMaghrib() + "\n" +
                getIsha() + "\n" +
                getNotificaiton() + "\n" +
                getNotificaitonType());
    }

    public Boolean isNight() {
        try {

            currentTime = Calendar.getInstance();

            if (getSunrise() != null || getMaghrib() != null) {

                cTime = new SimpleDateFormat("HH:mm").format(new Date()).split(":");

                currentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(cTime[0].trim()));
                currentTime.set(Calendar.MINUTE, Integer.parseInt(cTime[1].trim()));
                Log.i("Prayer/Current Time ", cTime[0] + ":" + cTime[1]);

                if (currentTime.before(sunsetTime) && currentTime.after(sunriseTime)) {
                    return false;
                } else
                    return true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isNextSunrise() {

        try {

            currentTime = Calendar.getInstance();
            cTime = new SimpleDateFormat("HH:mm").format(new Date()).split(":");

            currentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(cTime[0].trim()));
            currentTime.set(Calendar.MINUTE, Integer.parseInt(cTime[1].trim()));

            if (currentTime.before(sunriseTime) && currentTime.after(fajerTime)) {

                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean isNextAPrayer() {
        if (isNextMidnight() || isNextSunrise()) {
            return false;
        } else
            return true;
    }

    public Boolean isNextMidnight() {

        try {

            currentTime = Calendar.getInstance();
            cTime = new SimpleDateFormat("HH:mm").format(new Date()).split(":");

            currentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(cTime[0].trim()));
            currentTime.set(Calendar.MINUTE, Integer.parseInt(cTime[1].trim()));

            if (currentTime.before(midNightTime) && currentTime.after(ishaTime)) {

                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean calculateTimeBetweenPrayers() {

        try {
            currentTime = Calendar.getInstance();

            cTime = new SimpleDateFormat("HH:mm").format(new Date()).split(":");

            currentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(cTime[0].trim()));
            currentTime.set(Calendar.MINUTE, Integer.parseInt(cTime[1].trim()));

            // to update calender instances if the times are updated
            this.setCalenderPrayersTime();

            if (currentTime.before(fajerTime)) {
                setRemmainingPrayerTime(tools.difTimes(fajerTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.pFajer));
                return true;
            } else if (currentTime.before(sunriseTime)) {
                setRemmainingPrayerTime(tools.difTimes(sunriseTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.pDSunrise));
                return true;
            } else if (currentTime.before(duhrTime)) {
                setRemmainingPrayerTime(tools.difTimes(duhrTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.pDuhr));
                return true;
            } else if (currentTime.before(asrTime)) {
                setRemmainingPrayerTime(tools.difTimes(asrTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.pAsr));
                return true;
            } else if (currentTime.before(sunsetTime)) {
                setRemmainingPrayerTime(tools.difTimes(sunsetTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.pMaghrib));
                return true;
            } else if (currentTime.before(ishaTime)) {
                setRemmainingPrayerTime(tools.difTimes(ishaTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.pIsha));
                return true;
            } else if (currentTime.before(midNightTime) || currentTime.equals(midNightTime)) {
                setRemmainingPrayerTime(tools.difTimes(midNightTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.midnight_title));
                return true;
            } else {
                Log.e("Calc1", "The value is False");
                return false;
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.e("Calc2", "The value is False");
            return false;
        }

    }

    public Boolean notificationCheckPrayers() {

        try {
            cTime = new SimpleDateFormat("HH:mm").format(new Date()).split(":");

            currentTime = Calendar.getInstance();

            currentTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(cTime[0].trim()));
            currentTime.set(Calendar.MINUTE, Integer.parseInt(cTime[1].trim()));
            Log.i("Prayer/Current Time ", cTime[0] + ":" + cTime[1]);

            //update Calender Instances if the values changes
            this.setCalenderPrayersTime();

            if (tools.isEqualTime(currentTime, fajerTime)) {
                Log.i("Prayer/Current Time ", cTime[0] + ":" + cTime[1]);
                Log.i("Prayer/Fajer Time ", frTime[0] + ":" + frTime[1]);
                setRemmainingPrayerTime(tools.difTimes(fajerTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.pFajer));
                return true;
            } else if (tools.isEqualTime(currentTime, sunriseTime)) {
                Log.i("Prayer/Current Time ", cTime[0] + ":" + cTime[1]);
                Log.i("Prayer/Sunrise Time ", srTime[0] + ":" + srTime[1]);
                setRemmainingPrayerTime(tools.difTimes(sunriseTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.pSunrise));
                return true;
            } else if (tools.isEqualTime(currentTime, duhrTime)) {
                Log.i("Prayer/Current Time ", cTime[0] + ":" + cTime[1]);
                Log.i("Prayer/duhr Time ", duTime[0] + ":" + duTime[1]);
                setRemmainingPrayerTime(tools.difTimes(duhrTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.pDuhr));
                return true;
            } else if (tools.isEqualTime(currentTime, asrTime)) {
                Log.i("Prayer/Current Time ", cTime[0] + ":" + cTime[1]);
                Log.i("Prayer/Asr Time ", asTime[0] + ":" + asTime[1]);
                setRemmainingPrayerTime(tools.difTimes(asrTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.pAsr));
                return true;
            } else if (tools.isEqualTime(currentTime, sunsetTime)) {
                Log.i("Prayer/Current Time ", cTime[0] + ":" + cTime[1]);
                Log.i("Prayer/Sunset Time ", ssTime[0] + ":" + ssTime[1]);
                setRemmainingPrayerTime(tools.difTimes(sunsetTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.pMaghrib));
                return true;
            } else if (tools.isEqualTime(currentTime, ishaTime)) {
                Log.i("Prayer/Current Time ", cTime[0] + ":" + cTime[1]);
                Log.i("Prayer/Isha Time ", cTime[0] + ":" + cTime[1]);
                setRemmainingPrayerTime(tools.difTimes(ishaTime, currentTime));
                setCurrentPrayerName(getActivity().getString(R.string.pIsha));
                return true;
            } else {
                setCurrentPrayerName("");
                setRemmainingPrayerTime("");
                Log.i("Prayer/Equals Check ", "False");
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean dateText() {
        try {
            String day = "";
            String ssdate = null;
            String mmdate = null;
            Calendar calendar = Calendar.getInstance();
            int intDay = calendar.get(Calendar.DAY_OF_WEEK);

            switch (intDay) {
                case Calendar.SATURDAY:
                    day = getActivity().getString(R.string.dSat);
                    break;
                case Calendar.SUNDAY:
                    day = getActivity().getString(R.string.dSun);
                    break;
                case Calendar.MONDAY:
                    day = getActivity().getString(R.string.dMon);
                    break;
                case Calendar.TUESDAY:
                    day = getActivity().getString(R.string.dTue);
                    break;
                case Calendar.WEDNESDAY:
                    day = getActivity().getString(R.string.dWeb);
                    break;
                case Calendar.THURSDAY:
                    day = getActivity().getString(R.string.dThr);
                    break;
                case Calendar.FRIDAY:
                    day = getActivity().getString(R.string.dFri);
                    break;
                default:
                    break;
            }

            StringTokenizer sdate = new StringTokenizer(getSDate(), ".");
            StringTokenizer mdate = new StringTokenizer(getMDate(), ".");
            ssdate = sdate.nextToken() + " ";
            mmdate = mdate.nextToken() + " ";
            switch (Integer.parseInt(sdate.nextToken())) {
                case 1:
                    ssdate = ssdate + getActivity().getString(R.string.sMonthJan);
                    break;
                case 2:
                    ssdate = ssdate + getActivity().getString(R.string.sMonthFeb);
                    break;
                case 3:
                    ssdate = ssdate + getActivity().getString(R.string.sMonthMar);
                    break;
                case 4:
                    ssdate = ssdate + getActivity().getString(R.string.sMonthApr);
                    break;
                case 5:
                    ssdate = ssdate + getActivity().getString(R.string.sMonthMay);
                    break;
                case 6:
                    ssdate = ssdate + getActivity().getString(R.string.sMonthJun);
                    break;
                case 7:
                    ssdate = ssdate + getActivity().getString(R.string.sMonthJul);
                    break;
                case 8:
                    ssdate = ssdate + getActivity().getString(R.string.sMonthAug);
                    break;
                case 9:
                    ssdate = ssdate + getActivity().getString(R.string.sMonthSep);
                    break;
                case 10:
                    ssdate = ssdate + getActivity().getString(R.string.sMonthOct);
                    break;
                case 11:
                    ssdate = ssdate + getActivity().getString(R.string.sMonthNov);
                    break;
                case 12:
                    ssdate = ssdate + getActivity().getString(R.string.sMonthDec);
                    break;
                default:
                    break;
            }

            switch (Integer.parseInt(mdate.nextToken())) {
                case 1:
                    mmdate = mmdate + getActivity().getString(R.string.mMonthMuhram);
                    break;
                case 2:
                    mmdate = mmdate + getActivity().getString(R.string.mMonthSafer);
                    break;
                case 3:
                    mmdate = mmdate + getActivity().getString(R.string.mMonthRabiAoul);
                    break;
                case 4:
                    mmdate = mmdate + getActivity().getString(R.string.mMonthRabiAker);
                    break;
                case 5:
                    mmdate = mmdate + getActivity().getString(R.string.mMonthGamadaAoul);
                    break;
                case 6:
                    mmdate = mmdate + getActivity().getString(R.string.mMonthGamadaAker);
                    break;
                case 7:
                    mmdate = mmdate + getActivity().getString(R.string.mMonthRajb);
                    break;
                case 8:
                    mmdate = mmdate + getActivity().getString(R.string.mMonthShaban);
                    break;
                case 9:
                    mmdate = mmdate + getActivity().getString(R.string.mMonthRamadan);
                    break;
                case 10:
                    mmdate = mmdate + getActivity().getString(R.string.mMonthShual);
                    break;
                case 11:
                    mmdate = mmdate + getActivity().getString(R.string.mMonthZoKada);
                    break;
                case 12:
                    mmdate = mmdate + getActivity().getString(R.string.mMonthZoHaga);
                    break;
                default:
                    break;
            }

            ssdate = ssdate + " " + sdate.nextToken();
            mmdate = mmdate + " " + mdate.nextToken();

            setDay(day);
            setMFullDate(mmdate);
            setSFullDate(ssdate);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isDayHasChanged(String date) {
        try {
            Date currentDate = new SimpleDateFormat("dd.MM.yyyy").parse(date);
            return DateUtils.isToday(currentDate.getTime() + DateUtils.DAY_IN_MILLIS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true; // to trigger an Update when the date is not determined
    }

    public Boolean isRamadan() {
        try {
            StringTokenizer mdate = new StringTokenizer(getMDate(), ".");
            mdate.nextToken();
            if (Integer.parseInt(mdate.nextToken()) == 9)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Uri getAthan() {
        switch (getNotificaitonType()) {
            case "full":
                return fullAthan;

            case "half":
                return halfAthan;

            case "tone":
                return null;

            case "silent":
                return silentAthan;

            default:
                break;
        }
        return null;
    }

    public boolean isValid() {
        return this.objectId != null && this.sDate != null;
    }
}
