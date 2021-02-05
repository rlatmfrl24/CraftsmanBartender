package com.soulkey.craftsmanbartender.lib.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.soulkey.craftsmanbartender.lib.common.Constants
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.IngredientUnit
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.MakingStyle
import com.soulkey.craftsmanbartender.lib.db.RecipeDao
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.Recipe
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class RecipeRepositoryImpl(private val recipeDao : RecipeDao, private val firestore: FirebaseFirestore) : RecipeRepository {
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

    override suspend fun loadBaseRecipes() {
        withContext(Dispatchers.IO) {
            recipeDao.clearAll()
            firestore.collection("baseRecipe").get().await().map { documentSnapshot ->
                val recipeBasic = Recipe(
                    recipeId = null,
                    name = documentSnapshot["name"] as String,
                    glass = documentSnapshot["glass"] as String,
                    garnish = documentSnapshot["garnish"] as String,
                    primaryMakingStyle = MakingStyle.valueOf(documentSnapshot["primaryMakingStyle"] as String),
                    secondaryMakingStyle = MakingStyle.valueOf(documentSnapshot["secondaryMakingStyle"] as String),
                    applyMockTest = true
                )
                val ingredientList =
                    (documentSnapshot["ingredients"] as List<*>).map { ingredient ->
                        ingredient as Map<*, *>
                        Ingredient(
                            ingredientId = null,
                            recipeBasicId = null,
                            name = ingredient["name"] as String,
                            amount = (ingredient["amount"] as Double).toFloat(),
                            unit = ingredient["unit"] as String
                        )
                    }
                Pair(recipeBasic, ingredientList)
            }.map {
                createRecipe(it.first, it.second)
            }
        }
    }

    override fun uploadRecipe(recipe: Recipe, ingredients: List<Ingredient>) {
        val ingredientList = ingredients.map {
            hashMapOf(
                "name" to it.name,
                "amount" to it.amount,
                "unit" to it.unit
            )
        }

        firestore.collection("baseRecipe").document(recipe.name)
            .set(
                hashMapOf(
                    "name" to recipe.name,
                    "glass" to recipe.glass,
                    "garnish" to recipe.garnish,
                    "primaryMakingStyle" to recipe.primaryMakingStyle.name,
                    "secondaryMakingStyle" to recipe.secondaryMakingStyle?.name,
                    "ingredients" to ingredientList
                )
            )
    }
}