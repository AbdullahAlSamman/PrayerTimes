package com.gals.prayertimes.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gals.prayertimes.databinding.FragmentPrivacyPolicyBinding
import com.gals.prayertimes.viewmodel.PrivacyPolicyViewModel
import com.gals.prayertimes.viewmodel.factory.PrivacyPolicyViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PrivacyPolicyFragment : Fragment() {
    private lateinit var viewModel: PrivacyPolicyViewModel
    private lateinit var binding: FragmentPrivacyPolicyBinding
    private lateinit var mListener: OnFragmentInteractionListener

    @Inject
    lateinit var viewModelFactory: PrivacyPolicyViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivacyPolicyBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this

        configureMVVM()

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

    override fun onDetach() {
        super.onDetach()
        mListener.run {}
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri?)
    }

    private fun configureMVVM() {
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[PrivacyPolicyViewModel::class.java]

        binding.viewModel = viewModel
    }

}