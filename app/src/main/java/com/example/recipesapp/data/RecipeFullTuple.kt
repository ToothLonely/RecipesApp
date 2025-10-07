package com.example.recipesapp.data

import androidx.room.ColumnInfo

data class RecipesTuple(
    val id: Int,
    val title: String,
    val method: List<String>,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "recipe_id") val recipeId: Int,
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
)