package com.soulkey.craftsmanbartender.ui.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityRecipeDetailBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeDetailActivity : BaseActivity() {

    private val recipeViewModel : RecipeViewModel by viewModel()
    private val binding: ActivityRecipeDetailBinding by lazy {
        DataBindingUtil.setContentView<ActivityRecipeDetailBinding>(this, R.layout.activity_recipe_detail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = recipeViewModel


    }
}