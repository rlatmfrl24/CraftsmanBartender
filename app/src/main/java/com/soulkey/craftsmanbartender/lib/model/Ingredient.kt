package com.soulkey.craftsmanbartender.lib.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val ingredientId: Long?,
    @ColumnInfo val name: String,
    @ColumnInfo val amount: Float,
    @ColumnInfo val unit: String
)
