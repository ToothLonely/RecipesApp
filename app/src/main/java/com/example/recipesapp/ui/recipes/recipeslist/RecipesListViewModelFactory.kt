package com.example.recipesapp.ui.recipes.recipeslist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecipesListViewModelFactory(
    private val categoryId: Int,
    private val categoryName: String?,
    private val categoryImageUrl: String?,
    val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesListViewModel::class.java)) {
            return RecipesListViewModel(
                categoryId,
                categoryName,
                categoryImageUrl,
                application
            ) as T
        }
        throw (IllegalArgumentException("unknown ViewModel class"))
    }
}