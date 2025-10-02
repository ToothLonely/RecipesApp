package com.example.recipesapp.data

import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RecipesRepository {
    private val client = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            Json.asConverterFactory("application/json; charset=UTF8".toMediaType())
        )
        .build()

    private val service = client.create(RecipeApiService::class.java)

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

    suspend fun getRecipesByIds(set: Set<String>): List<Recipe>? {
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
}
