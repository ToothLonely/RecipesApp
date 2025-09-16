package com.example.recipesapp.data

import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.Executors

sealed class Result {
    class Success<T>(var data: T) : Result() {
        val _data = data
    }

    class Error(var exception: Exception?) : Result()
}

interface RepositoryCallback {
    fun onComplete(result: Result, categories: List<Category>): List<Category>
}

object RecipesRepository {

    private val threadPool = Executors.newFixedThreadPool(COUNT_OF_THREADS)
    private val client = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            Json.asConverterFactory("application/json; charset=UTF8".toMediaType())
        )
        .build()

    private val service = client.create(RecipeApiService::class.java)

    fun getCategories(callback: RepositoryCallback): List<Category> {
        try {
            threadPool.execute {
                val response = service.getCategories().execute()
                val result = Result.Success<List<Category>>(response.body()!!)
                return@execute callback.onComplete(result, result.data)
            }
        } catch (e: Exception) {
            val result = Result.Error(e)
            callback.onComplete(result, emptyList())
            throw IllegalStateException("fasfs")
        }

    }

    fun getRecipesByCategoryId(id: Int?): List<Recipe> {
        return service.getRecipesByCategoryId(id).execute().body()
            ?: throw IllegalStateException("cannot get recipes by categoryId: $id")
    }

    fun getRecipeById(recipeId: Int): Recipe {
        return service.getRecipeById(recipeId).execute().body()
            ?: throw IllegalStateException("cannot get recipe by recipeId: $recipeId")
    }

    fun getRecipesByIds(set: Set<String>): List<Recipe> {
        return service.getRecipesByIds(set.joinToString(",")).execute().body()
            ?: throw IllegalStateException("cannot get recipes by ids")
    }

    fun getCategoryById(id: Int): Category {
        return service.getCategoryById(id).execute().body()
            ?: throw IllegalStateException("cannot get category by categoryId: $id")
    }


}