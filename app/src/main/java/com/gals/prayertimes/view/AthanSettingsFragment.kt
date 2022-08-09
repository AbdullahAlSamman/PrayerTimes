package com.gals.prayertimes.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.databinding.FragmentSettingsBinding
import com.gals.prayertimes.repository.Repository
import com.gals.prayertimes.repository.db.AppDB
import com.gals.prayertimes.repository.db.AppDB.Companion.getInstance
import com.gals.prayertimes.services.PrayersODNotificationService
import com.gals.prayertimes.utils.UtilsManager
import com.gals.prayertimes.viewmodel.AthanSettingsViewModel
import com.gals.prayertimes.viewmodel.factory.AthanSettingsViewModelFactory

class AthanSettingsFragment : Fragment() {
    private lateinit var viewModel: AthanSettingsViewModel
    private lateinit var viewModelFactory: AthanSettingsViewModelFactory
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var db: AppDB
    private lateinit var tools: UtilsManager
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = getInstance(requireActivity().baseContext)

        viewModelFactory = AthanSettingsViewModelFactory(
            Repository(db),
            requireContext()
        )
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[AthanSettingsViewModel::class.java]

        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.getSettings()

        tools = UtilsManager(requireContext())

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
            viewModel.stopMediaPlayer()

            if (viewModel.alarm.get()) {
                startStopNotificationService(false)
            } else {
                startStopNotificationService(true)
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