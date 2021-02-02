package com.soulkey.craftsmanbartender.ui.mocktest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulkey.craftsmanbartender.lib.data.RecipeRepository
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import kotlinx.coroutines.launch
import timber.log.Timber

class MockTestViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
    var testRecipes = mutableListOf<RecipeWithIngredient>()

    val isFirstRecipeComplete : MutableLiveData<Boolean> = MutableLiveData(false)
    val isSecondRecipeComplete : MutableLiveData<Boolean> = MutableLiveData(false)
    val isThirdRecipeComplete : MutableLiveData<Boolean> = MutableLiveData(false)

    val firstRecipe: MutableLiveData<RecipeWithIngredient> = MutableLiveData()
    val secondRecipe: MutableLiveData<RecipeWithIngredient> = MutableLiveData()
    val thirdRecipe: MutableLiveData<RecipeWithIngredient> = MutableLiveData()

    fun initializeTestRecipe() {
        viewModelScope.launch {
            testRecipes = recipeRepository.getAllRecipes().filter { it.basic.applyMockTest }.toMutableList()
            Timber.v("diver:/ $testRecipes")
            assignAllRecipe()
        }
    }

    fun assignRecipe(): RecipeWithIngredient? {
        return if (testRecipes.isNotEmpty()){
            testRecipes.shuffled()[0].also { recipe ->
                testRecipes.remove(recipe)
                return recipe
            }
        } else {
            null
        }
    }

    private fun assignAllRecipe() {
        listOf(firstRecipe, secondRecipe, thirdRecipe).map { target ->
            assignRecipe()?.let { recipe ->
                target.value = recipe
            }
        }
    }

}