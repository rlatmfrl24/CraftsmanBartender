package com.soulkey.craftsmanbartender.ui.recipe

import androidx.lifecycle.*
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.MakingStyle
import com.soulkey.craftsmanbartender.lib.data.RecipeRepository
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.Recipe
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel(){
    // for AddRecipeActivity
    val recipeName: MutableLiveData<String> = MutableLiveData()
    val recipeGlass: MutableLiveData<String> = MutableLiveData()
    val recipeGarnish: MutableLiveData<String> = MutableLiveData()
    val primaryMakingStyle: MutableLiveData<MakingStyle> = MutableLiveData()
    val secondaryMakingStyle: MutableLiveData<MakingStyle> = MutableLiveData()
    val applyMockTest: MutableLiveData<Boolean> = MutableLiveData(true)

    // for RecipeDetailActivity
    val recipeBasic: MutableLiveData<Recipe> = MutableLiveData()
    val ingredients : MutableLiveData<MutableList<Ingredient>> = MutableLiveData(mutableListOf())

    // for RecipeActivity
    val recipes : LiveData<List<RecipeWithIngredient>> = recipeRepository.getRecipes()

    // Initialize when RecipeDetailActivity-onCreate
    fun initializeRecipe(recipeWithIngredient: RecipeWithIngredient) {
        recipeBasic.value = recipeWithIngredient.basic
        ingredients.value = recipeWithIngredient.ingredients.toMutableList()
    }

    suspend fun resetRecipeList() {
        recipeRepository.loadBaseRecipes()
    }

    // Delete Recipe on RecipeDetailActivity
    fun deleteCurrentRecipe(){
        val deleteBasic = recipeBasic.value?: return
        val deleteIngredient = ingredients.value?: return
        viewModelScope.launch {
            recipeRepository.deleteRecipe(deleteBasic, deleteIngredient)
        }
    }

    // Add Ingredient Data on AddRecipeActivity
    fun addIngredient(ingredient: Ingredient) {
        ingredients.value?.toMutableList()?.apply {
            add(ingredient)
            ingredients.value = this
        }
    }

    // Remove Ingredient Data on AddRecipeActivity
    fun removeIngredient(ingredient: Ingredient) {
        ingredients.value = ingredients.value?.filter {
            it != ingredient
        }?.toMutableList()
    }

    // Update Recipe whether Apply to Mock Test
    fun setApplyToMockTest(value: Boolean) {
        val basicToApply = recipeBasic.value?: return
        viewModelScope.launch {
            recipeRepository.applyRecipeToMockTest(basicToApply, value)
        }
    }

    // Create Recipe and Save to Database
    fun createRecipe() {
        val recipeName = recipeName.value?: return
        val recipeGlass = recipeGlass.value?: return
        val primaryMakingStyle = primaryMakingStyle.value?: return
        val ingredients = ingredients.value?: return

        recipeBasic.value = Recipe(
            recipeId = null,
            name = recipeName,
            glass = recipeGlass,
            garnish = recipeGarnish.value,
            primaryMakingStyle = primaryMakingStyle,
            secondaryMakingStyle = secondaryMakingStyle.value,
            applyMockTest = applyMockTest.value!!
        ).also { recipe ->
            viewModelScope.launch(Dispatchers.IO){
                recipeRepository.createRecipe(recipe, ingredients)
            }
        }
    }
}