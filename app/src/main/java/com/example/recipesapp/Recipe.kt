package com.example.recipesapp

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: String,
    val imageUrl: String,
)

data class Ingredient(
    val quantity: Double,
    val unitOfMeasure: String,
    val description: String,
)