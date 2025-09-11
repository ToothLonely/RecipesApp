package com.example.recipesapp.model

import com.example.recipesapp.ui.recipes.recipe.RecipeViewModel
import kotlinx.serialization.Serializable

@Serializable
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