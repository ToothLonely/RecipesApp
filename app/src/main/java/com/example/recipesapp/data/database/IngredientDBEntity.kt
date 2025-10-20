package com.example.recipesapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredient",
    foreignKeys = [
        ForeignKey(
            entity = RecipeDBEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipe_id"]
        )
    ]
)
data class IngredientDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "recipe_id") val recipeId: Int,
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
)