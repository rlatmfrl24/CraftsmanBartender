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
        }
    }

    private fun assignRecipe(): RecipeWithIngredient? {
        return if (testRecipes.isNotEmpty()){
            val assignedRecipe = testRecipes.shuffled()[0]
            testRecipes.removeAt(0)
            return assignedRecipe
        } else {
            null
        }
    }

    fun assignFirstRecipe() {
        assignRecipe()?.let {
            Timber.v("diver:/ First Recipe -> $it")
            firstRecipe.value = it
        }
    }
    fun assignSecondRecipe(){
        assignRecipe()?.let {
            Timber.v("diver:/ Second Recipe -> $it")
            secondRecipe.value = it
        }
    }
    fun assignThirdRecipe(){
        assignRecipe()?.let {
            Timber.v("diver:/ Third Recipe -> $it")
            thirdRecipe.value = it
        }
    }

    fun assignAllRecipe() {
        assignFirstRecipe()
        assignSecondRecipe()
        assignThirdRecipe()
    }

}