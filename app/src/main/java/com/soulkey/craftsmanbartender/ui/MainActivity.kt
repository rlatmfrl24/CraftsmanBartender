package com.soulkey.craftsmanbartender.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityMainBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import com.soulkey.craftsmanbartender.ui.mocktest.MockTestActivity
import com.soulkey.craftsmanbartender.ui.recipe.RecipeActivity
import com.soulkey.craftsmanbartender.ui.recipe.RecipeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : BaseActivity() {
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("pref", MODE_PRIVATE)
    }

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val recipeViewModel: RecipeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            // Check First Fun
            val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
            if (isFirstRun) {
                binding.containerMain.visibility = View.INVISIBLE
                binding.progressbarRecipeLoading.visibility = View.VISIBLE
                recipeViewModel.resetRecipeList()
                sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
                binding.containerMain.visibility = View.VISIBLE
                binding.progressbarRecipeLoading.visibility = View.INVISIBLE
            }
            setTheme(R.style.Theme_CraftsmanBartender)
        }

        binding.cardRecipesBtn.setOnClickListener {
            Intent(this, RecipeActivity::class.java).apply {
                startActivity(this)
            }
        }
        binding.cardMostTestBtn.setOnClickListener {
            Intent(this, MockTestActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}