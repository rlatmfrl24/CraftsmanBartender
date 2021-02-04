package com.soulkey.craftsmanbartender.lib.data

import com.soulkey.craftsmanbartender.lib.db.RecipeDao
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.Recipe
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient

class RecipeRepositoryImpl(private val recipeDao : RecipeDao) : RecipeRepository {
    override fun getRecipes() = recipeDao.getRecipes()

    override suspend fun getAllRecipes(): List<RecipeWithIngredient> {
        return recipeDao.getAllRecipes()
    }

    override suspend fun createRecipe(recipe: Recipe, ingredients: List<Ingredient>) {
        recipeDao.createRecipe(recipe, ingredients)
    }

    override suspend fun deleteRecipe(recipe: Recipe, ingredients: List<Ingredient>) {
        recipeDao.deleteRecipe(recipe, ingredients)
    }

    override suspend fun applyRecipeToMockTest(recipe: Recipe, value: Boolean) {
        recipe
        .apply { applyMockTest = value }
        .also { recipeDao.updateRecipe(it) }
    }
}