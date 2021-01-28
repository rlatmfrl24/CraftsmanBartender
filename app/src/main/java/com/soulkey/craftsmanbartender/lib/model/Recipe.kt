package com.soulkey.craftsmanbartender.lib.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.MakingStyle

@Entity
data class Recipe(
    @PrimaryKey val recipeBasicId: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val style: MutableList<MakingStyle>,
    @ColumnInfo val glass: String,
    @ColumnInfo val garnish: String
)
