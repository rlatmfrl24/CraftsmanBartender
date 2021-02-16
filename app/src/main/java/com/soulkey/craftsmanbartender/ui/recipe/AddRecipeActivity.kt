package com.soulkey.craftsmanbartender.ui.recipe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityAddRecipeBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import com.soulkey.craftsmanbartender.lib.common.BaseUtil.Companion.makeRequiredInRed
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.MakingStyle
import com.soulkey.craftsmanbartender.lib.view.ViewUtil
import com.soulkey.craftsmanbartender.ui.adapter.AddRecipeIngredientListAdapter
import com.soulkey.craftsmanbartender.ui.adapter.MakingStyleAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddRecipeActivity : BaseActivity() {
    private val recipeViewModel : RecipeViewModel by viewModel()
    private val ingredientAdapter: AddRecipeIngredientListAdapter by lazy {
        AddRecipeIngredientListAdapter(recipeViewModel)
    }
    private val binding : ActivityAddRecipeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_add_recipe)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(R.id.layout_recipe_add)
            drawingViewId = R.id.layout_recipe_add
            containerColor = Color.WHITE
            fadeMode = MaterialContainerTransform.FADE_MODE_OUT
            interpolator = FastOutSlowInInterpolator()
            duration = 500L
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(R.id.layout_recipe_add)
            duration = 300L
        }

        super.onCreate(savedInstanceState)
        binding.viewModel = recipeViewModel
        binding.adapter = ingredientAdapter

        // Making Style Spinner Setting
        val makingStyleList = MakingStyle.values().map { it.name }
        val makeStyleAdapter = MakingStyleAdapter(this, R.layout.item_spinner_default, makingStyleList)
        binding.spinnerPrimaryMakingStyle.setAdapter(makeStyleAdapter)
        binding.spinnerSecondaryMakingStyle.setAdapter(makeStyleAdapter)

        // Set Required Field
        binding.tilRecipeName.makeRequiredInRed()
        binding.tilRecipeGlass.makeRequiredInRed()
        binding.tilRecipePrimaryMakingStyle.makeRequiredInRed()

        // Set Ingredient List
        recipeViewModel.ingredients.observe(this, {
            ingredientAdapter.submitList(it)
        })

        // Set Add Ingredient Action
        binding.containerAddIngredient.setOnClickListener {
            ViewUtil.addIngredientDialog(this, recipeViewModel).show()
        }
        
        // Add Ingredient Fab Button Action
        binding.tvAddBtn.setOnClickListener {
            if (checkInputValidation()) {
                recipeViewModel.createRecipe()
                supportFinishAfterTransition()
            }
        }
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