package com.soulkey.craftsmanbartender.lib.common

import androidx.room.TypeConverter

class Constants {
    companion object {
        enum class MakingStyle {
            Build,
            Stir,
            Float,
            Shake
        }
        class Converters {
            @TypeConverter
            fun makingStylesToString(makingStyleList: MutableList<MakingStyle>): String {
                return makingStyleList.joinToString(separator = "/") { it.name }
            }
            @TypeConverter
            fun stringToMakingStyles(value: String): MutableList<MakingStyle> {
                return value.split("/").map {
                    MakingStyle.valueOf(it)
                }.toMutableList()
            }
        }
    }
}