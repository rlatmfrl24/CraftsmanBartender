package com.soulkey.craftsmanbartender.lib.di

import android.app.Application
import androidx.room.Room
import com.facebook.stetho.Stetho
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.soulkey.craftsmanbartender.lib.data.RecipeRepository
import com.soulkey.craftsmanbartender.lib.data.RecipeRepositoryImpl
import com.soulkey.craftsmanbartender.lib.db.AppDatabase
import com.soulkey.craftsmanbartender.ui.mocktest.MockTestViewModel
import com.soulkey.craftsmanbartender.ui.recipe.RecipeViewModel
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
        single {
            Firebase.firestore
        }
        single { get<AppDatabase>().recipeDao() }
        single<RecipeRepository> { RecipeRepositoryImpl(get(), get()) }

        viewModel { RecipeViewModel(get()) }
        viewModel { MockTestViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}