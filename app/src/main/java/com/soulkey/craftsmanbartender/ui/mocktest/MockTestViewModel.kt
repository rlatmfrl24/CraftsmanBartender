package com.soulkey.craftsmanbartender.ui.mocktest

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulkey.craftsmanbartender.lib.common.BaseUtil.Companion.addSourceList
import com.soulkey.craftsmanbartender.lib.data.RecipeRepository
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import kotlinx.coroutines.launch
import timber.log.Timber

class MockTestViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
    var testRecipes = mutableListOf<RecipeWithIngredient>()

    val isFirstRecipeComplete : MutableLiveData<Boolean> = MutableLiveData(false)
    val isSecondRecipeComplete : MutableLiveData<Boolean> = MutableLiveData(false)
    val isThirdRecipeComplete : MutableLiveData<Boolean> = MutableLiveData(false)
    val isTimerFinished : MutableLiveData<Boolean> = MutableLiveData(false)

    val skipCocktail = mutableListOf<RecipeWithIngredient>()
    val recipeCheckCocktail = mutableListOf<RecipeWithIngredient>()

    val isTestFinished = MediatorLiveData<Boolean>().apply {
        addSourceList(
            isFirstRecipeComplete,
            isSecondRecipeComplete,
            isThirdRecipeComplete,
            isTimerFinished
        ) {
            (isFirstRecipeComplete.value!!
                && isSecondRecipeComplete.value!!
                && isThirdRecipeComplete.value!!)
            || isTimerFinished.value!!
        }
    }

    val firstRecipe: MutableLiveData<RecipeWithIngredient> = MutableLiveData()
    val secondRecipe: MutableLiveData<RecipeWithIngredient> = MutableLiveData()
    val thirdRecipe: MutableLiveData<RecipeWithIngredient> = MutableLiveData()

    fun initializeTestRecipe() {
        viewModelScope.launch {
            isTimerFinished.value = false
            isFirstRecipeComplete.value = false
            isSecondRecipeComplete.value = false
            isThirdRecipeComplete.value = false
            isTestFinished.value = false
            skipCocktail.clear()
            recipeCheckCocktail.clear()
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
        } else { null }
    }

    private fun assignAllRecipe() {
        listOf(firstRecipe, secondRecipe, thirdRecipe).map { target ->
            assignRecipe()?.let { recipe ->
                target.value = recipe
            }
        }
    }

}