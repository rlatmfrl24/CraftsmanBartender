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
    // for Create
    val recipeName: MutableLiveData<String> = MutableLiveData()
    val recipeGlass: MutableLiveData<String> = MutableLiveData()
    val recipeGarnish: MutableLiveData<String> = MutableLiveData()
    val primaryMakingStyle: MutableLiveData<MakingStyle> = MutableLiveData()
    val secondaryMakingStyle: MutableLiveData<MakingStyle> = MutableLiveData()
    val applyMockTest: MutableLiveData<Boolean> = MutableLiveData(true)

    // for Detail
    val recipeBasic: MutableLiveData<Recipe> = MutableLiveData()
    val ingredients : MutableLiveData<MutableList<Ingredient>> = MutableLiveData(mutableListOf())

    val recipes : LiveData<List<RecipeWithIngredient>> = recipeRepository.getRecipes()

    fun initializeRecipe(recipeWithIngredient: RecipeWithIngredient) {
        Timber.v("diver:/ $recipeWithIngredient")
        recipeBasic.value = recipeWithIngredient.basic
        ingredients.value = recipeWithIngredient.ingredients.toMutableList()
    }

    fun deleteCurrentRecipe(){
        val deleteBasic = recipeBasic.value?: return
        Timber.v("diver:/ $deleteBasic")
        viewModelScope.launch {
            recipeRepository.deleteRecipe(deleteBasic)
        }
    }

    fun combineMakingStylesToString(): String? {
        val basic = recipeBasic.value ?: return null
        val makingStyleString = basic.primaryMakingStyle.name
        val secondStyle = basic.secondaryMakingStyle?: return makingStyleString
        return makingStyleString + " / " + secondStyle.name
    }

    fun addIngredient(ingredient: Ingredient) {
        ingredients.value?.toMutableList()?.apply {
            add(ingredient)
            ingredients.value = this
        }
    }

    fun removeIngredient(ingredient: Ingredient) {
        ingredients.value = ingredients.value?.filter {
            it != ingredient
        }?.toMutableList()
    }

    fun setApplyToMockTest(value: Boolean) {
        val basicToApply = recipeBasic.value?: return
        viewModelScope.launch {
            recipeRepository.applyRecipeToMockTest(basicToApply, value)
        }
    }

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
            Timber.v("diver:/ recipeBasic -> $recipe")
            Timber.v("diver:/ ingredients -> $ingredients")
            viewModelScope.launch(Dispatchers.IO){
                recipeRepository.createRecipe(recipe, ingredients)
            }
        }
    }
}