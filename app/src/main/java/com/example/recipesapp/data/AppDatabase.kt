package com.example.recipesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipesapp.model.Category

@Database(
    version = 1,
    entities = [
        Category::class
    ]
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getCategoriesDao(): CategoriesDao

    abstract fun getRecipesDao(): RecipesDao
}