package com.example.recipesapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addIngredients(newIngredients: List<IngredientDBEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addRecipes(newRecipes: List<RecipeDBEntity>)

    @Query(
        """
            SELECT *
            FROM recipe AS r
            JOIN category AS c ON r.category_id = c.id
            WHERE c.id = :categoryId
        """
    )
    suspend fun getRecipesList(categoryId: Int): List<RecipeDBEntity>

    @Query(
        """
            SELECT r.id, r.title, r.method, r.image_url, ing.quantity, ing.unitOfMeasure, ing.description
            FROM recipe AS r
            JOIN ingredient AS ing ON r.id = ing.recipe_id 
            WHERE r.id = :recipeId
        """
    )
    suspend fun getRecipe(recipeId: Int): List<RecipeFullTuple>

    @Query(
        """
            SELECT r.id
            FROM recipe AS r
            JOIN ingredient AS ing ON r.id = ing.recipe_id 
            WHERE r.is_favorite = 1
        """
    )
    suspend fun getFavorites(): List<Int>

    @Query(
        """
            UPDATE recipe
            SET is_favorite = 1
            WHERE id = :recipeId
        """
    )
    suspend fun updateFavorite(recipeId: Int)
}