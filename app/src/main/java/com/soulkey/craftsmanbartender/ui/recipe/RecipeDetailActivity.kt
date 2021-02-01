package com.soulkey.craftsmanbartender.ui.recipe

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityRecipeDetailBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import com.soulkey.craftsmanbartender.ui.adapter.IngredientListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeDetailActivity : BaseActivity() {

    private val recipeViewModel : RecipeViewModel by viewModel()
    private val ingredientAdapter = IngredientListAdapter()
    private val binding: ActivityRecipeDetailBinding by lazy {
        DataBindingUtil.setContentView<ActivityRecipeDetailBinding>(this, R.layout.activity_recipe_detail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = recipeViewModel
        binding.adapter = ingredientAdapter

        // Initialize Recipe to ViewModel
        intent.getParcelableExtra<RecipeWithIngredient>("recipe")?.also { recipe->
            recipeViewModel.initializeRecipe(recipe)
        }

        // Set Ingredients to Recyclerview
        recipeViewModel.ingredients.observe(this, Observer { list ->
            ingredientAdapter.submitList(list)
        })

        // Apply to Mock Test Button Setting
        binding.checkboxApplyMockTest.setOnCheckedChangeListener { _, isChecked ->
            recipeViewModel.setApplyToMockTest(isChecked)
        }

        // Toolbar - Back Button Action
        binding.toolbarRecipeDetail.setNavigationOnClickListener {
            finish()
        }

        // Toolbar - Delete Button Action
        binding.toolbarRecipeDetail.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.menu_recipe_detail_delete -> {
                    //Recipe Delete
                    recipeViewModel.deleteCurrentRecipe()
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}