package com.gals.prayertimes.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.R
import com.gals.prayertimes.databinding.FragmentSettingsBinding
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.db.AppDB
import com.gals.prayertimes.repository.db.AppDB.Companion.getInstance
import com.gals.prayertimes.repository.db.entities.Settings
import com.gals.prayertimes.services.MusicPlayer
import com.gals.prayertimes.services.PrayersODNotificationService
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.viewmodel.AthanSettingsViewModel
import com.gals.prayertimes.viewmodel.factory.AthanSettingsViewModelFactory

class AthanSettingsFragment : Fragment() {
    private lateinit var viewModel: AthanSettingsViewModel
    private lateinit var viewModelFactory: AthanSettingsViewModelFactory
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var db: AppDB
    private lateinit var musicFullAthanIntent: Intent
    private lateinit var musicHalfAthanIntent: Intent
    private lateinit var tools: UtilsManager
    private lateinit var playFullAthan: ImageButton
    private lateinit var playHalfAthan: ImageButton
    private lateinit var settings: Settings
    private lateinit var turnOnAlarm: SwitchCompat
    private var mListener: OnFragmentInteractionListener? = null
    private var playerHalfAthanOn = false
    private var playerFullAthanOn = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = getInstance(requireActivity().baseContext)

        viewModelFactory = AthanSettingsViewModelFactory(
            Repository(db)
        )
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[AthanSettingsViewModel::class.java]

        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.getSettings()

        /**View Elements id's*/
        playFullAthan = binding.playFullAthan
        playHalfAthan = binding.playHalfAthan
        turnOnAlarm = binding.switchAlarmOn
        tools = UtilsManager(requireContext())

        musicFullAthanIntent = Intent(
            context,
            MusicPlayer::class.java
        ).putExtra(
            "athanType",
            R.raw.fullathan
        )
        musicHalfAthanIntent = Intent(
            context,
            MusicPlayer::class.java
        ).putExtra(
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

    override fun onDestroyView() {
        super.onDestroyView()
        /**Save the Values and Stop Music if running*/
        try {
            viewModel.saveSettings()
            if (viewModel.alarm.get()) {
                startStopNotificationService(false)
            } else {
                startStopNotificationService(true)
            }
            if (tools.isServiceRunning(MusicPlayer::class.java)) {
                context?.stopService(musicFullAthanIntent)
                context?.stopService(musicHalfAthanIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
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

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri?)
    }

    companion object {
        fun newInstance(): AthanSettingsFragment =
            AthanSettingsFragment()
    }
}