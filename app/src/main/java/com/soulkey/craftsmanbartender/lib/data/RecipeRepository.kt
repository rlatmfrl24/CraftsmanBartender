package com.soulkey.craftsmanbartender.lib.data

import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.Recipe

interface RecipeRepository {
    suspend fun createRecipe(recipe: Recipe, ingredients: List<Ingredient>): Boolean
}