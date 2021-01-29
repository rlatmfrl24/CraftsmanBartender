package com.soulkey.craftsmanbartender.ui

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulkey.craftsmanbartender.lib.common.Constants
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.MakingStyle
import com.soulkey.craftsmanbartender.lib.data.RecipeRepository
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.Recipe
import kotlinx.coroutines.launch
import timber.log.Timber

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel(){
    val recipeName: MutableLiveData<String> = MutableLiveData()
    val recipeGlass: MutableLiveData<String> = MutableLiveData()
    val recipeGarnish: MutableLiveData<String> = MutableLiveData()
    val primaryMakingStyle: MutableLiveData<MakingStyle> = MutableLiveData()
    val secondaryMakingStyle: MutableLiveData<MakingStyle> = MutableLiveData()

    val ingredients : MutableLiveData<MutableList<Ingredient>> = MutableLiveData(mutableListOf())

    fun addIngredient(ingredient: Ingredient) {
        Timber.v("diver:/ ingredient")
        ingredients.value?.toMutableList()?.apply {
            add(ingredient)
            ingredients.value = this
        }
    }

    suspend fun createRecipe() {
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
            recipeRepository.createRecipe(recipe, ingredients)
        }
    }
}