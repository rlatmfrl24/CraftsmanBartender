package com.soulkey.craftsmanbartender.lib.di

import android.app.Application
import androidx.room.Room
import com.soulkey.craftsmanbartender.lib.data.RecipeRepository
import com.soulkey.craftsmanbartender.lib.data.RecipeRepositoryImpl
import com.soulkey.craftsmanbartender.lib.db.AppDatabase
import com.soulkey.craftsmanbartender.lib.db.RecipeDao
import com.soulkey.craftsmanbartender.ui.RecipeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    private val appModule = module {
        single {
            Room.databaseBuilder(applicationContext, AppDatabase::class.java,"app_database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
        single { get<AppDatabase>().recipeDao() }
        single<RecipeRepository> { RecipeRepositoryImpl(get()) }

        viewModel { RecipeViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}