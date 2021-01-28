package com.soulkey.craftsmanbartender.lib.model

import androidx.room.*

data class RecipeWithIngredient(
    @Embedded val basic: Recipe,
    @Relation(
        parentColumn = "recipeBasicId",
        entityColumn = "ingredientId"
    )
    val ingredients: List<Ingredient>
)
