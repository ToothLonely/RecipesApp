package com.example.recipesapp.model

import android.os.Parcelable
import com.example.recipesapp.ui.recipes.RecipeViewModel
import kotlinx.parcelize.Parcelize

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
)

fun Recipe.toRecipeState(): RecipeViewModel.RecipeState {
    return RecipeViewModel.RecipeState(
        id,
        title,
        ingredients,
        method,
        imageUrl,
    )
}