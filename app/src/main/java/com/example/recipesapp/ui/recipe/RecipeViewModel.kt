package com.example.recipesapp.ui.recipe

import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Ingredient

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val id: Int? = null,
        val title: String? = null,
        val ingredients: List<Ingredient> = listOf(),
        val method: List<String> = listOf(),
        val imageUrl: String? = null,
    )

}