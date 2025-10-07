package com.example.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addIngredients(newIngredients: List<IngredientDBEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipes(newRecipes: List<RecipeDBEntity>)

    @Query(
        """
            SELECT r.id, r.title, r.image_url
            FROM recipe AS r
            JOIN category AS c ON r.category_id = c.id
            WHERE c.id = :categoryId
        """
    )
    suspend fun getRecipesList(categoryId: Int): List<RecipeDBEntity>

    @Query(
        """
            SELECT * 
            FROM recipe AS r
            JOIN ingredient AS ing ON r.id = ing.recipe_id 
            WHERE r.id = :recipeId
        """
    )
    suspend fun getRecipe(recipeId: Int): List<RecipeFullTuple>

    fun convertToRecipe(result: List<RecipeFullTuple>): Recipe {
        val ingredients = result.map {
            Ingredient(
                quantity = it.quantity,
                unitOfMeasure = it.unitOfMeasure,
                description = it.description,
            )
        }

        return Recipe(
            id = result[0].id,
            title = result[0].title,
            ingredients = ingredients,
            method = result[0].method,
            imageUrl = result[0].imageUrl
        )
    }
}