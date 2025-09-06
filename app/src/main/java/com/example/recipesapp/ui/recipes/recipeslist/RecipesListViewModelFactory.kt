package com.example.recipesapp.ui.recipes.recipeslist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.model.Category

class RecipesListViewModelFactory(
    private val category: Category,
    val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesListViewModel::class.java)) {
            return RecipesListViewModel(
                category,
                application
            ) as T
        }
        throw (IllegalArgumentException("unknown ViewModel class"))
    }
}