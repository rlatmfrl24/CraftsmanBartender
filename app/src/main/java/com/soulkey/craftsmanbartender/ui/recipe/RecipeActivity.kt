package com.soulkey.craftsmanbartender.ui.recipe

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityRecipeBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import com.soulkey.craftsmanbartender.ui.adapter.RecipeListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeActivity : BaseActivity() {

    private val recipeAdapter = RecipeListAdapter()
    private val recipeViewModel: RecipeViewModel by viewModel()
    private val binding : ActivityRecipeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_recipe)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.adapter = recipeAdapter

        // Toolbar Menu Action
        binding.toolbarRecipe.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_recipe_reset -> {
                    // Load Base Recipes from Firestore
                    recipeViewModel.resetRecipeList()
                    true
                }
                else -> false
            }
        }

        // Apply Recipes to Recyclerview
        recipeViewModel.recipes.observe(this, { list->
            recipeAdapter.submitList(list)
        })

        // Add Recipe Fab Button action
        binding.fabAddRecipe.setOnClickListener {
            Intent(this, AddRecipeActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}