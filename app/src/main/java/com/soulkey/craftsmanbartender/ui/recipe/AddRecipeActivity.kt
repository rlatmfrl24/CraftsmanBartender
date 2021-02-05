package com.soulkey.craftsmanbartender.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.textfield.TextInputLayout
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityAddRecipeBinding
import com.soulkey.craftsmanbartender.databinding.DialogAddIngredientBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import com.soulkey.craftsmanbartender.lib.common.BaseUtil.Companion.makeRequiredInRed
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.IngredientUnit
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.MakingStyle
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.ui.adapter.AddRecipeIngredientListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddRecipeActivity : BaseActivity() {
    private val recipeViewModel : RecipeViewModel by viewModel()
    private val ingredientAdapter: AddRecipeIngredientListAdapter by lazy {
        AddRecipeIngredientListAdapter(recipeViewModel)
    }
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
        recipeViewModel.ingredients.observe(this, Observer {
            ingredientAdapter.submitList(it)
        })

        // Set Add Ingredient Action
        binding.containerAddIngredient.setOnClickListener {
            addIngredientDialog().show()
        }
        
        // Add Ingredient Fab Button Action
        binding.tvAddBtn.setOnClickListener {
            if (checkInputValidation()) {
                recipeViewModel.createRecipe()
                finish()
            }
        }
    }

    private fun checkInputValidation(): Boolean {
        val isInputValid =
                !recipeViewModel.recipeName.value.isNullOrBlank()
                && !recipeViewModel.recipeGlass.value.isNullOrBlank()
                && recipeViewModel.primaryMakingStyle.value != null
                && !recipeViewModel.ingredients.value.isNullOrEmpty()

        if (isInputValid) {
            if (recipeViewModel.secondaryMakingStyle.value == null) {
                recipeViewModel.secondaryMakingStyle.value = MakingStyle.None
            }
            if (recipeViewModel.recipeGarnish.value.isNullOrBlank()) {
                recipeViewModel.recipeGarnish.value = "None"
            }
        } else {
            if (recipeViewModel.recipeName.value.isNullOrBlank()) {
                binding.tilRecipeName.error = "Please Insert Recipe's Name"
            } else {
                binding.tilRecipeName.isErrorEnabled = false
            }
            if (recipeViewModel.recipeGlass.value.isNullOrBlank()) {
                binding.tilRecipeGlass.error = "Please Insert Glass for Cocktail"
            } else {
                binding.tilRecipeGlass.isErrorEnabled = false
            }
            if (recipeViewModel.primaryMakingStyle.value == null) {
                binding.tilRecipePrimaryMakingStyle.error = "You must choice Primary Making Style"
            } else {
                binding.tilRecipePrimaryMakingStyle.isErrorEnabled = false
            }
            if (recipeViewModel.ingredients.value.isNullOrEmpty()) {
                binding.containerErrorMsgIngredient.visibility = View.VISIBLE
            } else {
                binding.containerErrorMsgIngredient.visibility = View.INVISIBLE
            }
        }

        return isInputValid
    }

    // Add Ingredient Dialog Setting
    private fun addIngredientDialog(): MaterialDialog {

        val dialogBinding: DialogAddIngredientBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.dialog_add_ingredient,
                null,
                false
            )

        return MaterialDialog(this)
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
                                        dialogBinding.etIngredientAmount.text?.toString().isNullOrEmpty() -> { 0f }
                                        dialogBinding.etIngredientAmount.text?.toString().equals("-") -> { 0f }
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
                    val unitList = IngredientUnit.values().map { it.name }
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
                }
    }
}