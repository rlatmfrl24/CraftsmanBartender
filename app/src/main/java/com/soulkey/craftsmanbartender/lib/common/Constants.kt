package com.soulkey.craftsmanbartender.lib.common

import androidx.room.TypeConverter

class Constants {
    companion object {
        enum class MakingStyle {
            Build,
            Stir,
            Float,
            Shake,
            Blend,
            None
        }
        enum class IngredientUnit {
            oz,
            dash,
            tbsp,
            ea,
            part,
            fill
        }

        class Converters {
            @TypeConverter
            fun makingStyleToString(makingStyle: MakingStyle?): String? {
                return makingStyle?.name
            }
            @TypeConverter
            fun stringToMakingStyles(value: String?): MakingStyle {
                return if (value.isNullOrEmpty()) {
                    MakingStyle.None
                } else {
                    MakingStyle.valueOf(value)
                }
            }
        }
    }
}