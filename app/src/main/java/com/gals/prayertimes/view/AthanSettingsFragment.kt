package com.gals.prayertimes.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.R
import com.gals.prayertimes.databinding.FragmentSettingsBinding
import com.gals.prayertimes.model.NotificationType
import com.gals.prayertimes.repository.db.AppDB
import com.gals.prayertimes.repository.db.AppDB.Companion.getInstance
import com.gals.prayertimes.repository.db.entities.Settings
import com.gals.prayertimes.services.MusicPlayer
import com.gals.prayertimes.services.PrayersODNotificationService
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.view.custom.CustomRadioGroup
import com.gals.prayertimes.viewmodel.AthanSettingsViewModel
import com.gals.prayertimes.viewmodel.factory.AthanSettingsViewModelFactory
import java.util.*

class AthanSettingsFragment : Fragment() {
    private lateinit var viewModel: AthanSettingsViewModel
    private lateinit var viewModelFactory: AthanSettingsViewModelFactory
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var db: AppDB
    private lateinit var musicFullAthanIntent: Intent
    private lateinit var musicHalfAthanIntent: Intent
    private lateinit var tools: UtilsManager
    private lateinit var radioGroup: CustomRadioGroup
    private lateinit var radioFullAthan: RadioButton
    private lateinit var radioHalfAthan: RadioButton
    private lateinit var radioToneAthan: RadioButton
    private lateinit var radioSilentAthan: RadioButton
    private lateinit var playFullAthan: ImageButton
    private lateinit var playHalfAthan: ImageButton
    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var settings: Settings
    private var playerHalfAthanOn = false
    private var playerFullAthanOn = false
    lateinit var turnOnAlarm: Switch

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = getInstance(requireActivity().baseContext)
        GetSettingsFromDB().execute(this.context)

        viewModelFactory = AthanSettingsViewModelFactory()
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[AthanSettingsViewModel::class.java]

        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        /**View Elements id's*/
        radioFullAthan = binding.radioFullAthan
        radioHalfAthan = binding.radioHalfAthan
        radioToneAthan = binding.radioToneAthan
        radioSilentAthan = binding.radioSilentAthan
        playFullAthan = binding.playFullAthan
        playHalfAthan = binding.playHalfAthan
        turnOnAlarm = binding.switchAlarmOn
        tools = UtilsManager(requireContext())

        /*  radioGroup = view?.let {
              CustomRadioGroup(
                  it,
                  R.id.radioFullAthan,
                  R.id.radioHalfAthan,
                  R.id.radioToneAthan,
                  R.id.radioSilentAthan
              )
          }!!*/
        turnOnAlarm.setOnClickListener {
            if (turnOnAlarm.isChecked) {
                radioGroup.setAllEnabled()
            } else {
                radioGroup.setAllDisabled()
            }
        }
        musicFullAthanIntent = Intent(
            context,
            MusicPlayer::class.java
        )
        musicFullAthanIntent.putExtra(
            "athanType",
            R.raw.fullathan
        )
        musicHalfAthanIntent = Intent(
            context,
            MusicPlayer::class.java
        )
        musicHalfAthanIntent.putExtra(
            "athanType",
            R.raw.halfathan
        )
        playFullAthan.setOnClickListener {
            if (!playerFullAthanOn) {
                playerFullAthanOn = true
                playHalfAthan.isEnabled = false
                context?.startService(musicFullAthanIntent)
                playFullAthan.setImageResource(android.R.drawable.ic_media_pause)
            } else if (playerFullAthanOn) {
                playerFullAthanOn = false
                playHalfAthan.isEnabled = true
                context?.stopService(musicFullAthanIntent)
                playFullAthan.setImageResource(android.R.drawable.ic_media_play)
            }
        }
        playHalfAthan.setOnClickListener {
            if (!playerHalfAthanOn) {
                playFullAthan.isEnabled = false
                playerHalfAthanOn = true
                context?.startService(musicHalfAthanIntent)
                playHalfAthan.setImageResource(android.R.drawable.ic_media_pause)
            } else if (playerHalfAthanOn) {
                playerHalfAthanOn = false
                playFullAthan.isEnabled = true
                context?.stopService(musicHalfAthanIntent)
                playHalfAthan.setImageResource(android.R.drawable.ic_media_play)
            }
        }

        return binding.root
    }

    private fun updateUI() {
        turnOnAlarm.isChecked = settings.isNotification
        if (!turnOnAlarm.isChecked) {
            radioGroup.setAllDisabled()
        }
        when (settings.notificationType) {
            NotificationType.FULL.value -> radioFullAthan.isChecked = true
            NotificationType.HALF.value -> radioHalfAthan.isChecked = true
            NotificationType.TONE.value -> radioToneAthan.isChecked = true
            NotificationType.SILENT.value -> radioSilentAthan.isChecked = true
            else -> {}
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
        //Save the Values and Stop Music if running
        try {
            settings.isNotification = turnOnAlarm.isChecked
            if (!settings.isNotification) {
                startStopNotificationService(false)
            } else {
                startStopNotificationService(true)
            }
            when (radioGroup.selectedRadio
                .contentDescription
                .toString()) {
                "full" -> settings.notificationType = "full"
                "half" -> settings.notificationType = "half"
                "tone" -> settings.notificationType = "tone"
                "silent" -> settings.notificationType = "silent"
                else -> {}
            }
            if (tools.isServiceRunning(MusicPlayer::class.java)) {
                context?.stopService(musicFullAthanIntent)
                context?.stopService(musicHalfAthanIntent)
            }
            SettingsToDB().execute(this.context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startStopNotificationService(start: Boolean) {
        //TODO: check if useful and change
        try {
            if (!start) {
                val pService = Intent(
                    context,
                    PrayersODNotificationService::class.java
                )
                context?.stopService(pService)
            } else {
                val pService = Intent(
                    context,
                    PrayersODNotificationService::class.java
                )
                context?.startService(pService)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get data from db.
     */
    inner class GetSettingsFromDB : AsyncTask<Context?, String?, String?>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Context?): String? {
            try {
                settings = db.settingsDao.settings!!
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            updateUI()
        }
    }

    /**
     * Update settings db
     * load all countries data from db to memory objects to be displayed in recycler view.
     */
    @SuppressLint("StaticFieldLeak")
    inner class SettingsToDB : AsyncTask<Context?, String?, String?>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Context?): String? {
            try {
                Objects.requireNonNull(db.settingsDao).update(settings)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri?)
    }

    companion object {
        fun newInstance(): AthanSettingsFragment =
            AthanSettingsFragment()
    }
}