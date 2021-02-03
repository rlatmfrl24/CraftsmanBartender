package com.soulkey.craftsmanbartender.ui.mocktest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.FragmentTestResultBinding
import com.soulkey.craftsmanbartender.ui.adapter.ResultRecipeListAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TestResultFragment : Fragment() {
    private lateinit var binding: FragmentTestResultBinding
    private val mockTestViewModel : MockTestViewModel by sharedViewModel()
    private val skippedRecipesAdapter = ResultRecipeListAdapter()
    private val hintCheckedRecipesAdapter = ResultRecipeListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test_result, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Skipped & Hint Checked Recipes RecyclerView Setting
        binding.skippedRecipesAdapter = skippedRecipesAdapter
        binding.hintCheckedRecipesAdapter = hintCheckedRecipesAdapter
        skippedRecipesAdapter.submitList(mockTestViewModel.skipCocktail)
        hintCheckedRecipesAdapter.submitList(mockTestViewModel.recipeCheckCocktail)
        if (mockTestViewModel.skipCocktail.isNullOrEmpty()) {
            binding.recyclerSkippedRecipes.visibility = View.GONE
        }
        if (mockTestViewModel.recipeCheckCocktail.isNullOrEmpty()) {
            binding.recyclerHintCheckedRecipes.visibility = View.GONE
        }

        // Test Recipe Name Setting
        binding.tvFirstRecipeName.text = mockTestViewModel.firstRecipe.value?.basic?.name
        binding.tvSecondRecipeName.text = mockTestViewModel.secondRecipe.value?.basic?.name
        binding.tvThirdRecipeName.text = mockTestViewModel.thirdRecipe.value?.basic?.name

        // Test Recipes whether it was completed
        mockTestViewModel.isFirstRecipeComplete.observe(viewLifecycleOwner, Observer {
            when(it) {
                true -> binding.ivFirstRecipeIsCompleted.setImageResource(R.drawable.ic_check_circle_24px)
                false -> binding.ivFirstRecipeIsCompleted.setImageResource(R.drawable.ic_circle_24px)
            }
        })
        mockTestViewModel.isSecondRecipeComplete.observe(viewLifecycleOwner, Observer {
            when(it) {
                true -> binding.ivSecondRecipeIsCompleted.setImageResource(R.drawable.ic_check_circle_24px)
                false -> binding.ivSecondRecipeIsCompleted.setImageResource(R.drawable.ic_circle_24px)
            }
        })
        mockTestViewModel.isThirdRecipeComplete.observe(viewLifecycleOwner, Observer {
            when(it) {
                true -> binding.ivThirdRecipeIsCompleted.setImageResource(R.drawable.ic_check_circle_24px)
                false -> binding.ivThirdRecipeIsCompleted.setImageResource(R.drawable.ic_circle_24px)
            }
        })

        // Test Result Fragment Action Button Setting
        binding.buttonRetryTest.setOnClickListener {
            findNavController().navigate(TestResultFragmentDirections.actionTestResultFragmentToCountDownFragment())
        }
        binding.buttonTestDone.setOnClickListener {
            activity?.finish()
        }
    }
}