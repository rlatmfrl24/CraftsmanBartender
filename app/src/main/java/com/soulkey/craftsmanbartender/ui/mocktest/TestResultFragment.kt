package com.soulkey.craftsmanbartender.ui.mocktest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.FragmentTestResultBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TestResultFragment : Fragment() {
    private lateinit var binding: FragmentTestResultBinding
    private val mockTestViewModel : MockTestViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test_result, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonRetryTest.setOnClickListener {
            findNavController().navigate(TestResultFragmentDirections.actionTestResultFragmentToCountDownFragment())
        }
        binding.buttonTestDone.setOnClickListener {
            activity?.finish()
        }
    }
}