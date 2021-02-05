package com.soulkey.craftsmanbartender.lib.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeWithIngredient(
    @Embedded val basic: Recipe,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "recipeBasicId"
    )
    val ingredients: List<Ingredient>
) : Parcelable
