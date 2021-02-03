package com.soulkey.craftsmanbartender.ui.mocktest

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.FragmentMockTestBinding
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MockTestFragment : Fragment() {
    companion object {
        const val MINUTE_UNIT = 1000 * 60
        const val SECOND_UNIT = 1000
    }

    private lateinit var binding: FragmentMockTestBinding
    private val mockTestViewModel : MockTestViewModel by sharedViewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mock_test, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = mockTestViewModel
        mockTestViewModel.initializeTestRecipe()

        val timer = object : CountDownTimer((7L * MINUTE_UNIT), 1) {
            override fun onTick(millisUntilFinished: Long) {
                val leftMinute = (millisUntilFinished / MINUTE_UNIT).toString()
                val leftSecond = ((millisUntilFinished % MINUTE_UNIT) / SECOND_UNIT).toString().padStart(2, '0')
                val leftMill = (millisUntilFinished % SECOND_UNIT).toString().padStart(3, '0')
                val timerText = "$leftMinute:$leftSecond.$leftMill"
                binding.tvTimer.text = timerText
            }
            override fun onFinish() {
                lifecycleScope.launchWhenResumed {
                    // Go to Test Result
                }
            }
        }
        timer.start()

        // Complete Toggle Button Setting
        linkToggleWithData(mockTestViewModel.isFirstRecipeComplete, binding.ivFirstRecipeComplete)
        linkToggleWithData(mockTestViewModel.isSecondRecipeComplete, binding.ivSecondRecipeComplete)
        linkToggleWithData(mockTestViewModel.isThirdRecipeComplete, binding.ivThirdRecipeComplete)

        // Reroll Button Setting
        binding.ivFirstRecipeReroll.setOnClickListener {
            rerollRecipe(mockTestViewModel.firstRecipe)
        }
        binding.ivSecondRecipeReroll.setOnClickListener {
            rerollRecipe(mockTestViewModel.secondRecipe)
        }
        binding.ivThirdRecipeReroll.setOnClickListener {
            rerollRecipe(mockTestViewModel.thirdRecipe)
        }
    }

    private fun rerollRecipe(target: MutableLiveData<RecipeWithIngredient>) {
        if (mockTestViewModel.testRecipes.isNotEmpty()){
            target.value = mockTestViewModel.assignRecipe()
        } else {
            Toast.makeText(context, "더이상 Recipe 를 교체할 수 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun linkToggleWithData(data: MutableLiveData<Boolean>, view: ImageView) {
        data.observe(viewLifecycleOwner, Observer { isCompleted->
            when(isCompleted) {
                true -> view.setImageResource(R.drawable.ic_check_circle_24px)
                false -> view.setImageResource(R.drawable.ic_circle_24px)
            }
        })
        view.setOnClickListener {
            data.value?.let { oldValue->
                data.value = !oldValue
            }
        }
    }
}