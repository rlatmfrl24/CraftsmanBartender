package com.soulkey.craftsmanbartender.ui.recipe

import android.os.Bundle
import android.widget.ArrayAdapter
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddRecipeActivity : BaseActivity() {
    private val recipeViewModel : RecipeViewModel by viewModel()
    private val ingredientAdapter = IngredientListAdapter()
    private val binding : ActivityAddRecipeBinding by lazy {
        DataBindingUtil.setContentView<ActivityAddRecipeBinding>(this, R.layout.activity_add_recipe)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = recipeViewModel
        binding.adapter = ingredientAdapter

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
        recipeViewModel.ingredients.observe(this) {
            ingredientAdapter.submitList(it)
        }

        // Set Add Ingredient Action
        binding.containerAddIngredient.setOnClickListener {
            addIngredientDialog().show()
        }

        // Add Ingredient Fab Button Action
        binding.tvAddBtn.setOnClickListener {
            recipeViewModel.createRecipe()
            finish()
        }
    }

    // Add Ingredient Dialog Setting
    private fun addIngredientDialog(): MaterialDialog {
        return MaterialDialog(this)
                .title(text = "Add Ingredient")
                .customView(R.layout.dialog_add_ingredient, scrollable = true)
                .noAutoDismiss()
                .positiveButton {
                    it.til_ingredient_name.isErrorEnabled = false
                    it.til_ingredient_unit.isErrorEnabled = false

                    when {
                        it.et_ingredient_name.text.isNullOrBlank() -> {
                            it.til_ingredient_name.isErrorEnabled = true
                            it.til_ingredient_name.error = "Ingredient Name is empty"
                        }
                        it.spinner_ingredient_unit.text.isNullOrBlank() -> {
                            it.til_ingredient_unit.isErrorEnabled = true
                            it.til_ingredient_unit.error = "Ingredient Unit must be selected"
                        }
                        else -> {
                            val amount =
                                    when {
                                        it.et_ingredient_amount.text?.toString().isNullOrEmpty() -> {
                                            0f
                                        }
                                        it.et_ingredient_amount.text?.toString().equals("-") -> {
                                            0f
                                        }
                                        else -> it.et_ingredient_amount.text?.toString()?.toFloat()
                                    }
                            Ingredient(
                                    ingredientId = null,
                                    recipeBasicId = null,
                                    name = it.et_ingredient_name.text.toString(),
                                    amount = amount,
                                    unit = it.spinner_ingredient_unit.text.toString()
                            ).also { newIngredient ->
                                recipeViewModel.addIngredient(newIngredient)
                                it.dismiss()
                            }
                        }
                    }
                }
                .negativeButton {
                    it.dismiss()
                }
                .apply {
                    val unitList = IngredientUnit.values().map { it.name }
                    spinner_ingredient_unit.setAdapter(ArrayAdapter(context, R.layout.item_spinner_default, unitList))
                    spinner_ingredient_unit.setOnItemClickListener { parent, _, position, _ ->
                        val selected = (parent.getItemAtPosition(position) as String)
                        if (selected == "fill") {
                            et_ingredient_amount.isEnabled = false
                            et_ingredient_amount.setText("-")
                        } else {
                            et_ingredient_amount.isEnabled = true
                        }
                    }
                }
    }
}