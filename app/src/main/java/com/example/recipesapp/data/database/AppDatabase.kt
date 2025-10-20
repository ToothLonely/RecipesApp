package com.example.recipesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipesapp.model.Category

@Database(
    version = 3,
    entities = [
        Category::class,
        IngredientDBEntity::class,
        RecipeDBEntity::class,
    ]
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getCategoriesDao(): CategoriesDao

    abstract fun getRecipesDao(): RecipesDao
}