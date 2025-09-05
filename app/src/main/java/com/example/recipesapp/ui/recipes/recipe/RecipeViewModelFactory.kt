package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecipeViewModelFactory(val recipeId: Int, val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(recipeId, application) as T
        }
        throw(IllegalArgumentException("unknown ViewModel class"))
    }
}