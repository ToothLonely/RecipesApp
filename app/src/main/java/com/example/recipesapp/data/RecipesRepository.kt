package com.example.recipesapp.data

import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.model.toIngredientDBEntity
import com.example.recipesapp.model.toRecipeDBEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipesRepository @Inject constructor(
    private val recipesDao: RecipesDao,
    private val categoriesDao: CategoriesDao,
    private val service: RecipeApiService,
) {
    suspend fun getCategories(): List<Category>? {
        return try {
            withContext(Dispatchers.IO) {
                service.getCategories()
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getRecipesByCategoryId(id: Int?): List<Recipe>? {
        return try {
            withContext(Dispatchers.IO) {
                service.getRecipesByCategoryId(id)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            withContext(Dispatchers.IO) {
                service.getRecipeById(recipeId)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getRecipesByIds(set: Set<Int>): List<Recipe>? {
        return try {
            withContext(Dispatchers.IO) {
                service.getRecipesByIds(set.joinToString(","))
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCategoryById(id: Int): Category? {
        return try {
            withContext(Dispatchers.IO) {
                service.getCategoryById(id)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCategoriesFromCache() = withContext(Dispatchers.IO) {
        categoriesDao.getCategories()
    }

    suspend fun addCategories(newCategories: List<Category>) = withContext(Dispatchers.IO) {
        categoriesDao.addCategories(newCategories)
    }

    suspend fun getRecipesFromCache(categoryId: Int) = withContext(Dispatchers.IO) {
        recipesDao.getRecipesList(categoryId).map { it.toRecipe() }
    }

    private suspend fun addIngredients(ingredientsList: List<IngredientDBEntity>) =
        withContext(Dispatchers.IO) {
            recipesDao.addIngredients(ingredientsList)
        }

    suspend fun addRecipes(newRecipes: List<Recipe>, categoryId: Int) =
        withContext(Dispatchers.IO) {
            val recipesEntityList = newRecipes.map { it.toRecipeDBEntity(categoryId) }
            recipesDao.addRecipes(recipesEntityList)

            newRecipes.forEach { recipe ->
                val ingredientsEntityList = recipe.ingredients.map {
                    it.toIngredientDBEntity(recipe.id)
                }

                addIngredients(ingredientsEntityList)
            }
        }

    suspend fun getRecipeFromCache(recipeId: Int) = withContext(Dispatchers.IO) {
        val tuple = recipesDao.getRecipe(recipeId)
        convertToRecipe(tuple)
    }

    fun convertToRecipe(result: List<RecipeFullTuple>): Recipe? {
        if (result.isEmpty()) return null

        val ingredients = result.map {
            Ingredient(
                quantity = it.quantity,
                unitOfMeasure = it.unitOfMeasure,
                description = it.description,
            )
        }

        val recipe = result.first()
        return Recipe(
            id = recipe.id,
            title = recipe.title,
            ingredients = ingredients,
            method = recipe.method.split(CONVERTATION_DELIMITER),
            imageUrl = recipe.imageUrl
        )
    }

    suspend fun getFavoritesSet() = withContext(Dispatchers.IO) {
        recipesDao.getFavorites().toMutableSet()
    }

    suspend fun saveFavorites(favorites: List<Int>) = withContext(Dispatchers.IO) {
        favorites.forEach { id ->
            recipesDao.updateFavorite(id)
        }
    }
}
