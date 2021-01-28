package com.soulkey.craftsmanbartender.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityAddRecipeBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity

class AddRecipeActivity : BaseActivity() {

    private val binding : ActivityAddRecipeBinding by lazy {
        DataBindingUtil.setContentView<ActivityAddRecipeBinding>(this, R.layout.activity_add_recipe)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvAddBtn.setOnClickListener {
            onBackPressed()
        }
    }
}