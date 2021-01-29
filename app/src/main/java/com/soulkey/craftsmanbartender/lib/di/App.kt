package com.soulkey.craftsmanbartender.lib.di

import android.app.Application
import androidx.room.Room
import com.soulkey.craftsmanbartender.lib.db.AppDatabase
import com.soulkey.craftsmanbartender.ui.RecipeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    private val appModule = module {
        single {
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "AppDatabase"
            ).build()
        }

        viewModel { RecipeViewModel() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}