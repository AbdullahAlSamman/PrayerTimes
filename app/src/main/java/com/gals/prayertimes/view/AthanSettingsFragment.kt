package com.gals.prayertimes.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.databinding.FragmentSettingsBinding
import com.gals.prayertimes.services.NotificationService
import com.gals.prayertimes.viewmodel.AthanSettingsViewModel
import com.gals.prayertimes.viewmodel.factory.AthanSettingsViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AthanSettingsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: AthanSettingsViewModelFactory

    private lateinit var viewModel: AthanSettingsViewModel
    private lateinit var binding: FragmentSettingsBinding
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[AthanSettingsViewModel::class.java]

        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.getSettings()

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
        /**Save the values and stop music if running*/
        try {
            viewModel.saveSettings()
            viewModel.stopMediaPlayer()
            viewModel.startNotificationService(NotificationService::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri?)
    }
}