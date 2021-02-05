package com.soulkey.craftsmanbartender.lib.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.soulkey.craftsmanbartender.lib.common.Constants
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.lib.model.Recipe

@Database(entities = [Recipe::class, Ingredient::class], version = 2)
@TypeConverters(Constants.Companion.Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}