package com.soulkey.craftsmanbartender.lib.common

import androidx.room.TypeConverter

class Constants {
    companion object {
        enum class MakingStyle {
            Build,
            Stir,
            Float,
            Shake,
            Shake_Build,
            Build_Float
        }
        class Converters {
            @TypeConverter
            fun makingStyleToString(makingStyle: MakingStyle): String {
                return makingStyle.name
            }
            @TypeConverter
            fun intToMakingStyles(value: String): MakingStyle {
                return MakingStyle.valueOf(value)
            }
        }
    }
}