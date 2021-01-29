package com.soulkey.craftsmanbartender.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityAddRecipeBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import com.soulkey.craftsmanbartender.lib.common.BaseUtil.Companion.makeRequiredInRed
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.IngredientUnit
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.MakingStyle
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.ui.adapter.IngredientListAdapter
import kotlinx.android.synthetic.main.dialog_add_ingredient.*
import kotlinx.android.synthetic.main.item_add_recipe_ingredient.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AddRecipeActivity : BaseActivity() {

    private val recipeViewModel : RecipeViewModel by viewModel()
    private val ingredientAdapter = IngredientListAdapter()
    private val binding : ActivityAddRecipeBinding by lazy {
        DataBindingUtil.setContentView<ActivityAddRecipeBinding>(this, R.layout.activity_add_recipe)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Making Style Spinner Setting
        val makingStyleList = MakingStyle.values().map { it.name }
        val makeStyleAdapter = ArrayAdapter(this, R.layout.item_spinner_default, makingStyleList)
        binding.spinnerPrimaryMakingStyle.setAdapter(makeStyleAdapter)
        binding.spinnerSecondaryMakingStyle.setAdapter(makeStyleAdapter)

        // Set Required Field
        binding.tilRecipeName.makeRequiredInRed()
        binding.tilRecipeGlass.makeRequiredInRed()
        binding.tilRecipePrimaryMakingStyle.makeRequiredInRed()

        // Set Ingredient List
        binding.recyclerAddRecipeIngredients.apply {
            adapter = ingredientAdapter
        }
        recipeViewModel.ingredients.observe(this) {
            Timber.v("diver:/ $it")
            ingredientAdapter.submitList(it)
        }

        // Set Button Actions
        binding.containerAddIngredient.setOnClickListener {
            MaterialDialog(this)
                .title(text = "Add Ingredient")
                .customView(R.layout.dialog_add_ingredient, scrollable = true)
                .positiveButton {
                    Toast.makeText(this, "Positive Clicked", Toast.LENGTH_SHORT).show()
                    Ingredient(
                        ingredientId = null,
                        name = it.et_ingredient_name.text.toString(),
                        amount = it.et_ingredient_amount.text.toString().toFloat(),
                        unit = it.spinner_ingredient_unit.text.toString()
                    ).also { newIngredient ->
                        Timber.v("diver:/ $newIngredient")
                        recipeViewModel.addIngredient(newIngredient)
                    }
                }
                .negativeButton {
                    Toast.makeText(this, "Negative Clicked", Toast.LENGTH_SHORT).show()
                }.apply {
                    val unitList = IngredientUnit.values().map { it.name }
                    spinner_ingredient_unit.setAdapter(ArrayAdapter(context, R.layout.item_spinner_default, unitList))
                }
                .show()
        }

        binding.tvAddBtn.setOnClickListener {
            //TODO Add Recipe to Database
            onBackPressed()
        }
    }
}