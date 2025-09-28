package com.example.recipesapp.data

import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
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

    fun getCategories(): List<Category>? {
        return try {
            service.getCategories().execute().body()
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipesByCategoryId(id: Int?): List<Recipe>? {
        return try {
            service.getRecipesByCategoryId(id).execute().body()
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            service.getRecipeById(recipeId).execute().body()
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipesByIds(set: Set<String>): List<Recipe>? {
        return try {
            service.getRecipesByIds(set.joinToString(",")).execute().body()
        } catch (e: Exception) {
            null
        }
    }

    fun getCategoryById(id: Int): Category? {
        return try {
            service.getCategoryById(id).execute().body()
        } catch (e: Exception) {
            null
        }
    }
}
