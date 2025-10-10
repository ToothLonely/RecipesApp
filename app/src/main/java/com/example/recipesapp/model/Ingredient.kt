package com.example.recipesapp.model

import android.os.Parcelable
import com.example.recipesapp.data.IngredientDBEntity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Ingredient(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
) : Parcelable

fun Ingredient.toIngredientDBEntity(recipeId: Int): IngredientDBEntity {
    return IngredientDBEntity(
        recipeId = recipeId,
        quantity = quantity,
        unitOfMeasure = unitOfMeasure,
        description = description,
    )
}