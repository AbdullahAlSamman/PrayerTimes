package com.gals.prayertimes.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.R
import com.gals.prayertimes.utils.DataManager
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.viewmodel.SplashscreenViewModel
import com.gals.prayertimes.viewmodel.factory.SplashscreenViewModelFactory

class Splashscreen : AppCompatActivity() {
    private lateinit var viewModel: SplashscreenViewModel
    private lateinit var viewModelFactory: SplashscreenViewModelFactory
    lateinit var tools: UtilsManager
    lateinit var toMain: Intent

    //TODO: handle exceptions well.
    //TODO: background services.
    //TODO: Testing.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        configureMVVM()

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

    private fun configureMVVM() {
        viewModelFactory = SplashscreenViewModelFactory()
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[SplashscreenViewModel::class.java]
    }
}