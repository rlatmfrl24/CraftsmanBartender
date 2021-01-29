package com.soulkey.craftsmanbartender.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityAddRecipeBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import com.soulkey.craftsmanbartender.lib.common.BaseUtil.Companion.makeRequiredInRed
import com.soulkey.craftsmanbartender.lib.common.Constants

class AddRecipeActivity : BaseActivity() {

    private val binding : ActivityAddRecipeBinding by lazy {
        DataBindingUtil.setContentView<ActivityAddRecipeBinding>(this, R.layout.activity_add_recipe)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Making Style Spinner Setting
        val makingStyleList = Constants.Companion.MakingStyle.values().map { it.name }
        val makeStyleAdapter = ArrayAdapter(this, R.layout.item_add_recipe_making_style, makingStyleList)
        binding.spinnerPrimaryMakingStyle.setAdapter(makeStyleAdapter)
        binding.spinnerSecondaryMakingStyle.setAdapter(makeStyleAdapter)

        // Set Required Field
        binding.tilRecipeName.makeRequiredInRed()
        binding.tilRecipeGlass.makeRequiredInRed()
        binding.tilRecipePrimaryMakingStyle.makeRequiredInRed()

        // Set Button Actions
        binding.containerAddIngredient.setOnClickListener {
            Toast.makeText(this, "Add Ingredient Dialog", Toast.LENGTH_SHORT).show()
        }

        binding.tvAddBtn.setOnClickListener {
            //TODO Add Recipe to Database
            onBackPressed()
        }
    }
}