package com.example.recipesapp.model

import com.example.recipesapp.data.CONVERTATION_DELIMITER
import com.example.recipesapp.data.RecipeDBEntity
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

fun Recipe.toRecipeDBEntity(categoryId: Int): RecipeDBEntity {
    return RecipeDBEntity(
        id = id,
        categoryId = categoryId,
        title = title,
        method = method.joinToString(CONVERTATION_DELIMITER),
        imageUrl = imageUrl
    )
}