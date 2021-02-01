package com.soulkey.craftsmanbartender.lib.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.MakingStyle
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Recipe(
        @PrimaryKey(autoGenerate = true) val recipeId: Long?,
        @ColumnInfo val name: String,
        @ColumnInfo val primaryMakingStyle: MakingStyle,
        @ColumnInfo val secondaryMakingStyle: MakingStyle?,
        @ColumnInfo val glass: String,
        @ColumnInfo val garnish: String?
) : Parcelable
