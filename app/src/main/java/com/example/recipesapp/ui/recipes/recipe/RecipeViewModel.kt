package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.DEFAULT_NUMBER_OF_PORTIONS
import com.example.recipesapp.data.FAVORITES
import com.example.recipesapp.data.FAVORITES_SET
import com.example.recipesapp.data.IMAGE_URL
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.model.toRecipeState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RecipeViewModel(private val recipeId: Int, application: Application) :
    AndroidViewModel(application) {

    private val _recipeLiveData = MutableLiveData<RecipeState>()
    val recipeLiveData: LiveData<RecipeState>
        get() = _recipeLiveData

    private var favoritesSet = mutableSetOf<Int>()

    private val repo = RecipesRepository(application)

    data class RecipeState(
        val id: Int? = null,
        val title: String? = null,
        val ingredients: List<Ingredient> = listOf(),
        val method: List<String> = listOf(),
        val imageUrl: String? = null,
        var isFavorite: Boolean? = null,
        var portionsCount: Int? = null,
        var recipeImage: String? = null,
    )

    init {
        viewModelScope.launch {
            favoritesSet = repo.getFavoritesSet()
            loadRecipe(recipeId)
        }
    }

    private suspend fun loadRecipe(recipeId: Int) {

        val cachedRecipe = repo.getRecipeFromCache(recipeId)
        if (cachedRecipe != null) updateUI(cachedRecipe)

        val backendRecipe = repo.getRecipeById(recipeId)
        if (backendRecipe != null) updateUI(backendRecipe)
    }

    private fun updateUI(recipe: Recipe) {
        val state = recipe.toRecipeState().apply {
            isFavorite = id in favoritesSet
            portionsCount = DEFAULT_NUMBER_OF_PORTIONS
            recipeImage = "$IMAGE_URL${recipe.imageUrl}"
        }

        _recipeLiveData.value = state
    }

    fun saveFavorites() {
        viewModelScope.launch {
            repo.saveFavorites(favoritesSet.toList())
        }
    }

    fun onFavoritesClicked() {
        val favoriteFlag: Boolean

        if (_recipeLiveData.value?.isFavorite == true) {
            favoriteFlag = false
            favoritesSet.remove(recipeId)
        } else {
            favoriteFlag = true
            favoritesSet.add(recipeId)
        }

        _recipeLiveData.value = _recipeLiveData.value?.copy(isFavorite = favoriteFlag)
    }

    fun setPortionsCount(newPortionsCount: Int) {
        _recipeLiveData.value = _recipeLiveData.value?.copy(portionsCount = newPortionsCount)
    }
}
