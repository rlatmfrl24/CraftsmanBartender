package com.soulkey.craftsmanbartender.lib.view

import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.DialogAddIngredientBinding
import com.soulkey.craftsmanbartender.databinding.DialogRecipeHintBinding
import com.soulkey.craftsmanbartender.lib.common.Constants
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import com.soulkey.craftsmanbartender.ui.adapter.IngredientListAdapter
import com.soulkey.craftsmanbartender.ui.recipe.RecipeViewModel

class ViewUtil {
    companion object {
        fun addIngredientDialog(context: Context, recipeViewModel: RecipeViewModel): MaterialDialog {
            val dialogBinding: DialogAddIngredientBinding =
                    DataBindingUtil.inflate(
                            LayoutInflater.from(context),
                            R.layout.dialog_add_ingredient,
                            null,
                            false
                    )

            return MaterialDialog(context)
                    .title(text = "Add Ingredient")
                    .customView(view = dialogBinding.root, scrollable = true)
                    .noAutoDismiss()
                    .positiveButton {
                        dialogBinding.tilIngredientName.isErrorEnabled = false
                        dialogBinding.tilIngredientUnit.isErrorEnabled = false

                        when {
                            dialogBinding.etIngredientName.text.isNullOrBlank() -> {
                                dialogBinding.tilIngredientName.isErrorEnabled = true
                                dialogBinding.tilIngredientName.error = "Ingredient Name is empty"
                            }
                            dialogBinding.spinnerIngredientUnit.text.isNullOrBlank() -> {
                                dialogBinding.tilIngredientUnit.isErrorEnabled = true
                                dialogBinding.tilIngredientUnit.error = "Ingredient Unit must be selected"
                            }
                            else -> {
                                val amount =
                                        when {
                                            dialogBinding.etIngredientAmount.text?.toString().isNullOrEmpty() -> 0f
                                            dialogBinding.etIngredientAmount.text?.toString().equals("-") -> 0f
                                            else -> dialogBinding.etIngredientAmount.text?.toString()?.toFloat()
                                        }
                                Ingredient(
                                        ingredientId = null,
                                        recipeBasicId = null,
                                        name = dialogBinding.etIngredientName.text.toString(),
                                        amount = amount,
                                        unit = dialogBinding.spinnerIngredientUnit.text.toString()
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
                        val unitList = Constants.Companion.IngredientUnit.values().map { it.name }
                        dialogBinding.spinnerIngredientUnit.setAdapter(ArrayAdapter(context, R.layout.item_spinner_default, unitList))
                        dialogBinding.spinnerIngredientUnit.setOnItemClickListener { parent, _, position, _ ->
                            val selected = (parent.getItemAtPosition(position) as String)
                            if (selected == "fill") {
                                dialogBinding.etIngredientAmount.isEnabled = false
                                dialogBinding.etIngredientAmount.setText("-")
                            } else {
                                dialogBinding.etIngredientAmount.isEnabled = true
                            }
                        }
                        dialogBinding.spinnerIngredientUnit.setOnFocusChangeListener { v, hasFocus ->
                            if (hasFocus) {
                                (v as AutoCompleteTextView).showDropDown()
                                context.getSystemService(Context.INPUT_METHOD_SERVICE).also { imm ->
                                    (imm as InputMethodManager).hideSoftInputFromWindow(v.windowToken, 0)
                                }
                            }
                        }
                    }
        }

        fun makeRecipeDialog(context: Context, recipe: RecipeWithIngredient): MaterialDialog {
            val dialogBinding: DialogRecipeHintBinding =
                    DataBindingUtil.inflate(
                            LayoutInflater.from(context),
                            R.layout.dialog_recipe_hint,
                            null,
                            false
                    )

            return MaterialDialog(context)
                    .title(text = recipe.basic.name)
                    .customView(view = dialogBinding.root, scrollable = true, horizontalPadding = true)
                    .apply {
                        val ingredientAdapter = IngredientListAdapter()
                        dialogBinding.tvMakingStyle.text = recipe.basic.combineMakingStylesToString()
                        dialogBinding.tvGlass.text = recipe.basic.glass
                        dialogBinding.tvGarnish.text = recipe.basic.garnish
                        dialogBinding.recyclerRecipeIngredients.adapter = ingredientAdapter
                        ingredientAdapter.submitList(recipe.ingredients)
                    }
                    .positiveButton()
        }
    }
}