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
    val method: List<String>,
    @ColumnInfo(name = "image_url") val imageUrl: String,
)

fun RecipeDBEntity.toRecipe() = Recipe(
    id = id,
    title = title,
    ingredients = listOf(),
    method = method,
    imageUrl = imageUrl,
)