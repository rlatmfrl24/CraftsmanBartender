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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.FragmentMockTestBinding
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import com.soulkey.craftsmanbartender.lib.view.ViewUtil
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MockTestFragment : Fragment() {
    companion object {
        const val SECOND_UNIT = 1000
        const val MINUTE_UNIT = SECOND_UNIT * 60
        const val TEST_TIME = 0.1
    }

    private lateinit var binding: FragmentMockTestBinding
    private var recipeHintDialog: MaterialDialog? = null
    private var rerollAlertDialog: MaterialDialog? = null
    private var recipeHintAlertDialog: MaterialDialog? = null

    private val mockTestViewModel : MockTestViewModel by sharedViewModel()
    private val timer: CountDownTimer by lazy {
        object : CountDownTimer((TEST_TIME * MINUTE_UNIT).toLong(), 1) {
            override fun onTick(millisUntilFinished: Long) {
                val leftMinute = (millisUntilFinished / MINUTE_UNIT).toString()
                val leftSecond = ((millisUntilFinished % MINUTE_UNIT) / SECOND_UNIT).toString().padStart(2, '0')
                val leftMill = (millisUntilFinished % SECOND_UNIT).toString().padStart(3, '0')
                val timerText = "$leftMinute:$leftSecond.$leftMill"
                binding.tvTimer.text = timerText
            }
            override fun onFinish() {
                lifecycleScope.launchWhenResumed {
                    mockTestViewModel.isTimerFinished.value = true
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mock_test, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = mockTestViewModel
        timer.start()

        // Test Finish Process
        mockTestViewModel.isTestFinished.observe(viewLifecycleOwner, {
            if (it) {
                lifecycleScope.launch {
                    timer.cancel()
                    recipeHintDialog?.dismiss()
                    rerollAlertDialog?.dismiss()
                    recipeHintAlertDialog?.dismiss()

                    // Go to Result Fragment
                    MaterialDialog(requireContext())
                            .cancelOnTouchOutside(false)
                            .cancelable(false)
                            .title(text = "Test Finish")
                            .message(text = "테스트가 종료되었습니다. 결과 화면으로 이동합니다")
                            .positiveButton(text = "확인", click = {
                                findNavController().navigate(MockTestFragmentDirections.actionMockTestFragmentToTestResultFragment())
                            }).show()
                }
            }
        })

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

        // Add Show Recipe Hint Action on Recipe Name
        binding.tvFirstRecipeName.setOnClickListener {
            alertShowRecipeHint(mockTestViewModel.firstRecipe.value!!)
        }
        binding.tvSecondRecipeName.setOnClickListener {
            alertShowRecipeHint(mockTestViewModel.secondRecipe.value!!)
        }
        binding.tvThirdRecipeName.setOnClickListener {
            alertShowRecipeHint(mockTestViewModel.thirdRecipe.value!!)
        }
    }

    // Link Toggle Button with Data that is whether Recipe Complete
    private fun linkToggleWithData(data: MutableLiveData<Boolean>, view: ImageView) {
        data.observe(viewLifecycleOwner, { isCompleted->
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

    private fun rerollRecipe(target: MutableLiveData<RecipeWithIngredient>) {
        rerollAlertDialog = makeRerollAlertDialog(target)
        rerollAlertDialog?.show()
    }

    private fun alertShowRecipeHint(recipe: RecipeWithIngredient) {
        recipeHintAlertDialog = makeRecipeHintDialog(recipe)
        recipeHintAlertDialog?.show()
    }

    private fun makeRecipeHintDialog(recipe: RecipeWithIngredient): MaterialDialog {
        return MaterialDialog(requireContext())
                .title(text = "Warning")
                .message(R.string.string_warning_recipe_hint_event_message)
                .positiveButton {
                    // Record Recipe Hint Check
                    if (!mockTestViewModel.recipeCheckCocktail.contains(recipe)) {
                        mockTestViewModel.recipeCheckCocktail.add(recipe)
                    }
                    //show recipe hint
                    recipeHintDialog = ViewUtil.makeRecipeDialog(requireContext(), recipe)
                    recipeHintDialog?.show()
                }
                .negativeButton()
    }

    private fun makeRerollAlertDialog(target: MutableLiveData<RecipeWithIngredient>): MaterialDialog {
        return MaterialDialog(requireContext())
                .title(text = "Skip Recipe")
                .message(R.string.string_reroll_recipe_msg)
                .positiveButton{
                    if (mockTestViewModel.testRecipes.isNotEmpty()){
                        mockTestViewModel.skipCocktail.add(target.value!!)
                        target.value = mockTestViewModel.assignRecipe()
                    } else {
                        Toast.makeText(context, "더이상 Recipe 를 교체할 수 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
                .negativeButton()
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
        recipeHintDialog?.cancel()
        rerollAlertDialog?.cancel()
        recipeHintAlertDialog?.dismiss()
    }
}