package com.soulkey.craftsmanbartender.lib.db

import androidx.room.*
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import com.soulkey.craftsmanbartender.lib.model.Recipe

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe, ingredients: List<Ingredient>)

    @Update
    suspend fun updateRecipe(vararg recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(vararg recipe: Recipe)

    @Transaction
    @Query("SELECT * FROM Recipe")
    suspend fun getRecipes(): List<RecipeWithIngredient>
}