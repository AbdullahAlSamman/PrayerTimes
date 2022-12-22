package com.gals.prayertimes.view

 import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.R
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.viewmodel.SplashscreenViewModel
import com.gals.prayertimes.viewmodel.factory.SplashscreenViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class Splashscreen : AppCompatActivity() {
    private lateinit var toMain: Intent
    private lateinit var viewModel: SplashscreenViewModel
    lateinit var tools: UtilsManager

    @Inject
    lateinit var viewModelFactory: SplashscreenViewModelFactory

    //TODO: handle exceptions well and handle no network situation.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        tools = UtilsManager(baseContext)

        configureMVVM()
        configureLoading()

        /**Change Status bar color*/
        tools.changeStatusBarColor(window)

        viewModel.prepareApp()
    }

    private fun configureMVVM() {
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