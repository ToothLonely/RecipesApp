package com.example.recipesapp.data

import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("category/{id}/recipes")
    fun getRecipesByCategoryId(@Path("id") id: Int?): Call<List<Recipe>>

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") recipeId: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipesByIds(@Query("ids") set: String): Call<List<Recipe>>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") id: Int): Call<Category>
}