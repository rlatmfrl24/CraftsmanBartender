package com.soulkey.craftsmanbartender.ui.recipe

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.databinding.DataBindingUtil
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityRecipeDetailBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import com.soulkey.craftsmanbartender.ui.adapter.IngredientListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeDetailActivity : BaseActivity() {

    private val recipeViewModel : RecipeViewModel by viewModel()
    private val ingredientAdapter = IngredientListAdapter()
    private val binding: ActivityRecipeDetailBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail)
    }
    private val updateRecipeLauncher = registerForActivityResult(StartActivityForResult()) { result->
        if (result.resultCode == Activity.RESULT_OK) {
            val updateId = result.data?.getLongExtra("updateId", -1)
            recipeViewModel.currentID.value = (updateId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(R.id.layout_recipe_detail)
            containerColor = Color.WHITE
            fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
            interpolator = FastOutSlowInInterpolator()
            duration = 400L
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(R.id.layout_recipe_detail)
            containerColor = Color.WHITE
            fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
            interpolator = FastOutSlowInInterpolator()
            duration = 400L
        }

        super.onCreate(savedInstanceState)
        binding.viewModel = recipeViewModel
        binding.adapter = ingredientAdapter
        binding.lifecycleOwner = this

        // Initialize Recipe to ViewModel
        recipeViewModel.currentID.value = intent.getLongExtra("recipeId", -1)
        recipeViewModel.currentRecipeWithIngredient.observe(this, {
            ingredientAdapter.submitList(it.ingredients)
        })

        // Apply to Mock Test Button Setting
        binding.checkboxApplyMockTest.setOnCheckedChangeListener { _, isChecked ->
            recipeViewModel.setApplyToMockTest(isChecked)
        }

        // Toolbar - Back Button Action
        binding.toolbarRecipeDetail.setNavigationOnClickListener {
            supportFinishAfterTransition()
        }

        // Toolbar - Delete Button Action
        binding.toolbarRecipeDetail.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.menu_recipe_detail_update -> {
                    updateRecipeLauncher.launch(Intent(this, UpdateRecipeActivity::class.java).apply {
                        putExtra("recipeId", recipeViewModel.currentID.value)
                    })
                    true
                }
                R.id.menu_recipe_detail_delete -> {
                    //Recipe Delete
                    MaterialDialog(this)
                        .title(text = "Recipe Delete")
                        .message(text = "정말로 해당 레시피를 삭제하시겠습니까?")
                        .positiveButton {
                            recipeViewModel.deleteCurrentRecipe()
                            supportFinishAfterTransition()
                        }
                        .negativeButton()
                        .show()
                    true
                }
                else -> false
            }
        }
    }
}