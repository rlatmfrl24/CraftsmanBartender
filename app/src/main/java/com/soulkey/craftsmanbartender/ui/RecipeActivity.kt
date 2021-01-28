package com.soulkey.craftsmanbartender.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityRecipeBinding
import com.soulkey.craftsmanbartender.ui.adapter.RecipeListAdapter

class RecipeActivity : AppCompatActivity() {
    private val binding : ActivityRecipeBinding by lazy {
        DataBindingUtil.setContentView<ActivityRecipeBinding>(this, R.layout.activity_recipe)
    }
    private val recipeAdapter = RecipeListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.recyclerRecipes.apply {
            adapter = recipeAdapter
        }

        binding.fabAddRecipe.setOnClickListener {
            Intent(this, AddRecipeActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}