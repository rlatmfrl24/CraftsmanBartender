package com.soulkey.craftsmanbartender.lib.data

import com.soulkey.craftsmanbartender.lib.db.RecipeDao
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.Recipe
import java.lang.Exception

class RecipeRepositoryImpl(private val recipeDao : RecipeDao) : RecipeRepository {
    override suspend fun createRecipe(recipe: Recipe, ingredients: List<Ingredient>): Boolean {
        return try {
            recipeDao.insertRecipe(recipe, ingredients)
            true
        } catch(exception: Exception) {
            false
        }
    }
}