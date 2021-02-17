package com.soulkey.craftsmanbartender.ui.recipe

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityUpdateRecipeBinding
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.MakingStyle
import com.soulkey.craftsmanbartender.lib.view.ViewUtil
import com.soulkey.craftsmanbartender.ui.adapter.AddRecipeIngredientListAdapter
import com.soulkey.craftsmanbartender.ui.adapter.MakingStyleAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateRecipeActivity : AppCompatActivity() {

    private val recipeViewModel: RecipeViewModel by viewModel()
    private val binding: ActivityUpdateRecipeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_update_recipe)
    }
    private val ingredientAdapter: AddRecipeIngredientListAdapter by lazy {
        AddRecipeIngredientListAdapter(recipeViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = recipeViewModel
        binding.adapter = ingredientAdapter


        recipeViewModel.currentID.value = intent.getLongExtra("recipeId", -1)
        recipeViewModel.currentRecipeWithIngredient.observe(this, {
            recipeViewModel.recipeName.value = it.basic.name
            recipeViewModel.recipeGlass.value = it.basic.glass
            recipeViewModel.recipeGarnish.value = it.basic.garnish
            recipeViewModel.primaryMakingStyle.value = it.basic.primaryMakingStyle
            recipeViewModel.secondaryMakingStyle.value = it.basic.secondaryMakingStyle
            recipeViewModel.applyMockTest.value = it.basic.applyMockTest
            recipeViewModel.ingredients.value = it.ingredients.toMutableList()
        })

        // Making Style Spinner Setting
        val makingStyleList = MakingStyle.values().map { it.name }
        val makeStyleAdapter = MakingStyleAdapter(this, R.layout.item_spinner_default, makingStyleList)
        binding.spinnerPrimaryMakingStyle.setAdapter(makeStyleAdapter)
        binding.spinnerSecondaryMakingStyle.setAdapter(makeStyleAdapter)

        // Set Add Ingredient Action
        binding.containerAddIngredient.setOnClickListener {
            ViewUtil.addIngredientDialog(this, recipeViewModel).show()
        }

        recipeViewModel.ingredients.observe(this, {
            ingredientAdapter.submitList(it)
        })

        binding.tvAddUpdate.setOnClickListener {
            lifecycleScope.launch {
                if (checkInputValidation()) {
                    recipeViewModel.updateRecipe()
                    intent.putExtra("updateId", recipeViewModel.currentID.value)
                    setResult(Activity.RESULT_OK, intent)
                    supportFinishAfterTransition()
                }
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }


    private fun checkInputValidation(): Boolean {
        val isInputValid =
                !recipeViewModel.recipeName.value.isNullOrBlank()
                        && !recipeViewModel.recipeGlass.value.isNullOrBlank()
                        && recipeViewModel.primaryMakingStyle.value != null
                        && !recipeViewModel.ingredients.value.isNullOrEmpty()

        if (isInputValid) {
            if (recipeViewModel.secondaryMakingStyle.value == null) {
                recipeViewModel.secondaryMakingStyle.value = MakingStyle.None
            }
            if (recipeViewModel.recipeGarnish.value.isNullOrBlank()) {
                recipeViewModel.recipeGarnish.value = "None"
            }
        } else {
            if (recipeViewModel.recipeName.value.isNullOrBlank()) {
                binding.tilRecipeName.error = "Please Insert Recipe's Name"
            } else {
                binding.tilRecipeName.isErrorEnabled = false
            }
            if (recipeViewModel.recipeGlass.value.isNullOrBlank()) {
                binding.tilRecipeGlass.error = "Please Insert Glass for Cocktail"
            } else {
                binding.tilRecipeGlass.isErrorEnabled = false
            }
            if (recipeViewModel.primaryMakingStyle.value == null) {
                binding.tilRecipePrimaryMakingStyle.error = "You must choice Primary Making Style"
            } else {
                binding.tilRecipePrimaryMakingStyle.isErrorEnabled = false
            }
            if (recipeViewModel.ingredients.value.isNullOrEmpty()) {
                binding.containerErrorMsgIngredient.visibility = View.VISIBLE
            } else {
                binding.containerErrorMsgIngredient.visibility = View.INVISIBLE
            }
        }

        return isInputValid
    }
}