package com.example.recipesapp.data

import androidx.room.ColumnInfo

data class RecipeFullTuple(
    val id: Int,
    val title: String,
    val method: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
)