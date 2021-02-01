package com.soulkey.craftsmanbartender.lib.data

import androidx.lifecycle.LiveData
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.Recipe
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun createRecipe(recipe: Recipe, ingredients: List<Ingredient>)
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun applyRecipeToMockTest(recipe: Recipe, value: Boolean)
    fun getRecipes(): LiveData<List<RecipeWithIngredient>>
}