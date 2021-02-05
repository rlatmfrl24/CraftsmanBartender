package com.soulkey.craftsmanbartender.lib.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "ingredients")
data class Ingredient(
        @PrimaryKey(autoGenerate = true) val ingredientId: Long?,
        @ColumnInfo var recipeBasicId: Long?,
        @ColumnInfo val name: String,
        @ColumnInfo val amount: Float?,
        @ColumnInfo val unit: String
) : Parcelable
