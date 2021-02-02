package com.soulkey.craftsmanbartender.ui.mocktest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.FragmentMockTestBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MockTestFragment : Fragment() {
    private lateinit var binding: FragmentMockTestBinding
    private val mockTestViewModel : MockTestViewModel by sharedViewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mock_test, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mockTestViewModel.initializeTestRecipe()
        mockTestViewModel.assignAllRecipe()

        // First Recipe Complete Action & View Setting
        mockTestViewModel.isFirstRecipeComplete.observe(viewLifecycleOwner, Observer {
            when(it) {
                true -> binding.ivFirstRecipeComplete.setImageResource(R.drawable.ic_check_circle_24px)
                false -> binding.ivFirstRecipeComplete.setImageResource(R.drawable.ic_circle_24px)
            }
        })
        binding.ivFirstRecipeComplete.setOnClickListener {
            mockTestViewModel.isFirstRecipeComplete.value?.let {
                mockTestViewModel.isFirstRecipeComplete.value = !it
            }
        }

        // Second Recipe Complete Action & View Setting
        mockTestViewModel.isSecondRecipeComplete.observe(viewLifecycleOwner, Observer {
            when(it) {
                true -> binding.ivSecondRecipeComplete.setImageResource(R.drawable.ic_check_circle_24px)
                false -> binding.ivSecondRecipeComplete.setImageResource(R.drawable.ic_circle_24px)
            }
        })
        binding.ivSecondRecipeComplete.setOnClickListener {
            mockTestViewModel.isSecondRecipeComplete.value?.let {
                mockTestViewModel.isSecondRecipeComplete.value = !it
            }
        }

        // Third Recipe Complete Action & View Setting
        mockTestViewModel.isThirdRecipeComplete.observe(viewLifecycleOwner, Observer {
            when(it) {
                true -> binding.ivThirdRecipeComplete.setImageResource(R.drawable.ic_check_circle_24px)
                false -> binding.ivThirdRecipeComplete.setImageResource(R.drawable.ic_circle_24px)
            }
        })
        binding.ivThirdRecipeComplete.setOnClickListener {
            mockTestViewModel.isThirdRecipeComplete.value?.let {
                mockTestViewModel.isThirdRecipeComplete.value = !it
            }
        }

        binding.ivFirstRecipeReroll.setOnClickListener {
            lifecycleScope.launch {
                mockTestViewModel.assignFirstRecipe()
            }
        }
        binding.ivSecondRecipeReroll.setOnClickListener {
            lifecycleScope.launch {
                mockTestViewModel.assignSecondRecipe()
            }
        }
        binding.ivThirdRecipeReroll.setOnClickListener {
            lifecycleScope.launch {
                mockTestViewModel.assignThirdRecipe()
            }
        }
    }
}