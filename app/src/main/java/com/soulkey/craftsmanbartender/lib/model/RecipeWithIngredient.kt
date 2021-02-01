package com.soulkey.craftsmanbartender.lib.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeWithIngredient(
    @Embedded val basic: Recipe,
    @Relation(
        parentColumn = "recipeBasicId",
        entityColumn = "ingredientId"
    )
    val ingredients: List<Ingredient>
) : Parcelable
