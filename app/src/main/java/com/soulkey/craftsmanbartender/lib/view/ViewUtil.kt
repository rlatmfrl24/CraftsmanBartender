package com.soulkey.craftsmanbartender.lib.view

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.DialogRecipeHintBinding
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import com.soulkey.craftsmanbartender.ui.adapter.IngredientListAdapter

class ViewUtil {
    companion object {
        fun makeRecipeDialog(context: Context, recipe: RecipeWithIngredient): MaterialDialog {
            val dialogBinding : DialogRecipeHintBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.dialog_recipe_hint,
                    null,
                    false
                )

            return MaterialDialog(context)
                .title(text = recipe.basic.name)
                .customView(view=dialogBinding.root, scrollable = true, horizontalPadding = true)
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