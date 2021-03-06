package com.soulkey.craftsmanbartender.ui.mocktest

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.FragmentCountDownBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CountDownFragment : Fragment() {
    private lateinit var binding: FragmentCountDownBinding
    private val mockTestViewModel: MockTestViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_count_down, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mockTestViewModel.initializeTestRecipe()

        val timer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCountDown.text = (millisUntilFinished / 1000).toString()
            }
            override fun onFinish() {
                lifecycleScope.launchWhenResumed {
                    findNavController().navigate(CountDownFragmentDirections.actionCountDownFragmentToMockTestFragment())
                }
            }
        }
        timer.start()
    }
}