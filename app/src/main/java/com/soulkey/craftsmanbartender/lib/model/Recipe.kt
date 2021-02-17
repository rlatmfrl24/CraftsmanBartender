package com.soulkey.craftsmanbartender.lib.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.MakingStyle
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Recipe(
        @PrimaryKey(autoGenerate = true) val recipeId: Long?,
        @ColumnInfo var name: String,
        @ColumnInfo var primaryMakingStyle: MakingStyle,
        @ColumnInfo var secondaryMakingStyle: MakingStyle?,
        @ColumnInfo var glass: String,
        @ColumnInfo var garnish: String?,
        @ColumnInfo var applyMockTest: Boolean
) : Parcelable {
        fun combineMakingStylesToString(): String {
                val makingStyleString = primaryMakingStyle.name
                val secondStyle = secondaryMakingStyle?: return makingStyleString
                return makingStyleString + " / " + secondStyle.name
        }
}
