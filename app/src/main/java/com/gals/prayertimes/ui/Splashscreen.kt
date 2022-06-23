package com.gals.prayertimes.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gals.prayertimes.R
import com.gals.prayertimes.utils.DataManager
import com.gals.prayertimes.utils.UtilsManager

class Splashscreen : AppCompatActivity() {
    lateinit var tools: UtilsManager
    lateinit var toMain: Intent

    //TODO: change to database.
    //TODO: handle exceptions well.
    //TODO: Testing.
    //TODO: remove old code.
    //TODO: background services.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        toMain = Intent(
            this,
            MainActivity::class.java
        )
        tools = UtilsManager(baseContext)

        /**Change Status bar color*/
        tools.changeStatusBarColor(window)
        DataManager(
            toMain,
            this.baseContext,
            false
        ).execute(
            "",
            "",
            ""
        )
    }
}