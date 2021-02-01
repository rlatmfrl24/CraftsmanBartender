package com.soulkey.craftsmanbartender.ui.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityRecipeBinding
import com.soulkey.craftsmanbartender.ui.adapter.RecipeListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeActivity : AppCompatActivity() {

    private val recipeAdapter = RecipeListAdapter()
    private val recipeViewModel: RecipeViewModel by viewModel()
    private val binding : ActivityRecipeBinding by lazy {
        DataBindingUtil.setContentView<ActivityRecipeBinding>(this, R.layout.activity_recipe)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.adapter = recipeAdapter

        recipeViewModel.recipes.observe(this) { list->
            recipeAdapter.submitList(list)
        }

        binding.fabAddRecipe.setOnClickListener {
            Intent(this, AddRecipeActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}