package com.gals.prayertimes.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.TypeConverters;

import com.gals.prayertimes.model.Prayer;
import com.gals.prayertimes.model.Settings;

@Database(entities = {Prayer.class, Settings.class}, version = 1, exportSchema = false)
//@TypeConverters(Converters.class)
public abstract class AppDB extends RoomDatabase {
    private static final String DB_NAME = "prayers-db";
    private static AppDB instance;

    /**
     * Gets instance.
     * Singleton implementation to forbid creating more than one data base connection.
     *
     * @param context the context of current activity.
     * @return the instance
     */
    public static synchronized AppDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                    , AppDB.class, DB_NAME).build();
        }
        return instance;
    }

    /**
     * Prayers dao.
     * implementation will be generated in compile time from Room Library.
     *
     * @return the Prayer dao
     */
    public abstract PrayerDao prayerDao();

    /**
     * Settings dao.
     * implementation will be generated in compile time from Room Library.
     *
     * @return the Settings dao
     */
    public abstract SettingsDao settingsDao();
}
