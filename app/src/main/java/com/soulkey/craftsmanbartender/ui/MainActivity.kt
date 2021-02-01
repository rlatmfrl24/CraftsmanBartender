package com.soulkey.craftsmanbartender.ui

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityMainBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import com.soulkey.craftsmanbartender.ui.recipe.RecipeActivity

class MainActivity : BaseActivity() {
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CraftsmanBartender)

        binding.cardRecipesBtn.setOnClickListener {
            Intent(this, RecipeActivity::class.java).apply {
                startActivity(this)
            }
        }
        binding.cardMostTestBtn.setOnClickListener {

        }
    }
}