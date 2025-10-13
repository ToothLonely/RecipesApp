package com.example.recipesapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipesapp.model.Recipe

@Entity(tableName = "recipe")
data class RecipeDBEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "category_id") val categoryId: Int,
    val title: String,
    val method: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
)

fun RecipeDBEntity.toRecipe() = Recipe(
    id = id,
    title = title,
    ingredients = listOf(),
    method = method.split(CONVERTATION_DELIMITER),
    imageUrl = imageUrl,
)