package com.soulkey.craftsmanbartender.lib.data

import androidx.lifecycle.LiveData
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.Recipe
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient

interface RecipeRepository {
    suspend fun createRecipe(recipe: Recipe, ingredients: List<Ingredient>)
    suspend fun updateRecipe(recipe: Recipe, ingredients: List<Ingredient>)
    suspend fun deleteRecipe(recipe: Recipe, ingredients: List<Ingredient>)
    suspend fun applyRecipeToMockTest(recipe: Recipe, value: Boolean)
    suspend fun getAllRecipes(): List<RecipeWithIngredient>
    suspend fun loadBaseRecipes()
    fun getRecipeById(id: Long): RecipeWithIngredient
    fun getRecipes(): LiveData<List<RecipeWithIngredient>>
    @Deprecated(message = "Developer Options") fun uploadRecipe(recipe: Recipe, ingredients: List<Ingredient>)

}