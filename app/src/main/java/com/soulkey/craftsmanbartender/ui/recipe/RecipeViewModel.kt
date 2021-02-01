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
    val recipeName: MutableLiveData<String> = MutableLiveData()
    val recipeGlass: MutableLiveData<String> = MutableLiveData()
    val recipeGarnish: MutableLiveData<String> = MutableLiveData()
    val primaryMakingStyle: MutableLiveData<MakingStyle> = MutableLiveData()
    val secondaryMakingStyle: MutableLiveData<MakingStyle> = MutableLiveData()
    val ingredients : MutableLiveData<MutableList<Ingredient>> = MutableLiveData(mutableListOf())

    val recipes : LiveData<List<RecipeWithIngredient>> = recipeRepository.getRecipes()

    fun initializeRecipe(recipeWithIngredient: RecipeWithIngredient) {
        recipeName.value = recipeWithIngredient.basic.name
        recipeGlass.value = recipeWithIngredient.basic.glass
        recipeGarnish.value = recipeWithIngredient.basic.garnish
        primaryMakingStyle.value = recipeWithIngredient.basic.primaryMakingStyle
        secondaryMakingStyle.value = recipeWithIngredient.basic.secondaryMakingStyle
        ingredients.value = recipeWithIngredient.ingredients.toMutableList()
    }

    fun addIngredient(ingredient: Ingredient) {
        ingredients.value?.toMutableList()?.apply {
            add(ingredient)
            ingredients.value = this
        }
    }

    fun createRecipe() {
        val recipeName = recipeName.value?: return
        val recipeGlass = recipeGlass.value?: return
        val primaryMakingStyle = primaryMakingStyle.value?: return
        val ingredients = ingredients.value?: return

        Recipe(
            recipeBasicId = null,
            name = recipeName,
            glass = recipeGlass,
            garnish = recipeGarnish.value,
            primaryMakingStyle = primaryMakingStyle,
            secondaryMakingStyle = secondaryMakingStyle.value
        ).also { recipe ->
            Timber.v("diver:/ recipeBasic -> $recipe")
            Timber.v("diver:/ ingredients -> $ingredients")
            viewModelScope.launch(Dispatchers.IO){
                recipeRepository.createRecipe(recipe, ingredients)
            }
        }
    }
}