package com.soulkey.craftsmanbartender.lib.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import com.soulkey.craftsmanbartender.lib.model.Recipe

@Dao
interface RecipeDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createRecipe(recipe: Recipe, ingredients: List<Ingredient>){
        val recipeId = insertRecipe(recipe)
        ingredients.map {
            it.apply { recipeBasicId = recipeId }
        }.also { insertIngredients(it) }
    }

    @Transaction
    @Update
    suspend fun updateRecipeWithIngredient(recipe: Recipe, ingredients: List<Ingredient>) {
        updateRecipe(recipe)
        deleteIngredients(getIngredientsByRecipeID(recipe.recipeId!!))
        ingredients.map {
            it.apply { recipeBasicId = recipe.recipeId }
        }.also { insertIngredients(ingredients) }
    }

    @Transaction
    @Query("SELECT * FROM Recipe WHERE recipeId LIKE :id")
    fun getRecipeWithIngredientByRecipeID(id: Long): RecipeWithIngredient

    @Query("SELECT * FROM ingredients WHERE recipeBasicId=:id")
    suspend fun getIngredientsByRecipeID(id: Long): List<Ingredient>

    @Transaction
    @Delete
    suspend fun deleteRecipe(recipe: Recipe, ingredients: List<Ingredient>) {
        deleteRecipeBasic(recipe)
        deleteIngredients(ingredients)
    }

    @Query("DELETE FROM Recipe")
    suspend fun clearRecipeTable()

    @Query("DELETE FROM ingredients")
    suspend fun clearIngredientTable()

    @Transaction
    suspend fun clearAll(){
        clearRecipeTable()
        clearIngredientTable()
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<Ingredient>)

    @Update
    suspend fun updateRecipe(vararg recipe: Recipe)

    @Delete
    suspend fun deleteRecipeBasic(vararg recipe: Recipe)

    @Delete
    suspend fun deleteIngredients(ingredients: List<Ingredient>)

    @Transaction
    @Query("SELECT * FROM Recipe")
    suspend fun getAllRecipes(): List<RecipeWithIngredient>

    @Transaction
    @Query("SELECT * FROM Recipe")
    fun getRecipes(): LiveData<List<RecipeWithIngredient>>


}