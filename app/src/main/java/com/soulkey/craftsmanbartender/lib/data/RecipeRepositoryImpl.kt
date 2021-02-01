package com.soulkey.craftsmanbartender.lib.data

import com.soulkey.craftsmanbartender.lib.db.RecipeDao
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.Recipe
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import timber.log.Timber
import java.lang.Exception

class RecipeRepositoryImpl(private val recipeDao : RecipeDao) : RecipeRepository {
    override fun getRecipes() = recipeDao.getRecipes()
    override suspend fun createRecipe(recipe: Recipe, ingredients: List<Ingredient>) {
        recipeDao.createRecipe(recipe, ingredients)
    }
    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe = *arrayOf(recipe))
    }
}