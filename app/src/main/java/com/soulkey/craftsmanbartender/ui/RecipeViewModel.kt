package com.soulkey.craftsmanbartender.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import timber.log.Timber

class RecipeViewModel : ViewModel(){
    val ingredients : MutableLiveData<MutableList<Ingredient>> = MutableLiveData(mutableListOf())

    fun addIngredient(ingredient: Ingredient) {
        Timber.v("diver:/ ingredient")
        ingredients.value?.toMutableList()?.apply {
            add(ingredient)
            ingredients.value = this
        }
    }
}