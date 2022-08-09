package com.gals.prayertimes.view

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.R
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.db.AppDB
import com.gals.prayertimes.services.NotificationService
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.viewmodel.SplashscreenViewModel
import com.gals.prayertimes.viewmodel.factory.SplashscreenViewModelFactory
import java.util.*

@SuppressLint("CustomSplashScreen")
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

        tools = UtilsManager(baseContext)

        configureMVVM()
        configureLoading()

        /**Change Status bar color*/
        tools.changeStatusBarColor(window)

        viewModel.updateData()

        startService(Intent(this, NotificationService::class.java))
        bindService(
            Intent(this, NotificationService::class.java),
            viewModel.timerServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (viewModel.isNotificationServiceBound) {
            unbindService(viewModel.timerServiceConnection)
        }
    }

    private fun configureMVVM() {
        viewModelFactory = SplashscreenViewModelFactory(
            repository = Repository(AppDB.getInstance(this))
        )
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[SplashscreenViewModel::class.java]
    }

    private fun configureLoading() {
        viewModel.loading.observe(this, Observer { status ->
            if (!status) {
                navigateToMain()
            }
        }
        )
    }

    private fun navigateToMain() {
        toMain = Intent(
            this,
            MainActivity::class.java
        )
        toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(toMain)
    }
}