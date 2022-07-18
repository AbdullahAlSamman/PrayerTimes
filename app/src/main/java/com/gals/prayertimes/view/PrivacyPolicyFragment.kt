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
import com.gals.prayertimes.viewmodel.factory.PrivacyPolicyFactory

class PrivacyPolicyFragment : Fragment() {
    private lateinit var viewModel: PrivacyPolicyViewModel
    private lateinit var viewModelFactory: PrivacyPolicyFactory
    private lateinit var binding: FragmentPrivacyPolicyBinding
    private lateinit var mListener: OnFragmentInteractionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivacyPolicyBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this

        configureMVVM()

        viewModel.loadUrl(binding)

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
        viewModelFactory = PrivacyPolicyFactory()
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[PrivacyPolicyViewModel::class.java]

        binding.viewModel = viewModel
    }

    companion object {
        fun newInstance(): PrivacyPolicyFragment {
            val fragment = PrivacyPolicyFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}