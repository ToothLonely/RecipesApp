package com.example.recipesapp.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.DEFAULT_NUMBER_OF_PORTIONS
import com.example.recipesapp.data.IMAGE_URL
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.model.toRecipeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repo: RecipesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val recipeId = savedStateHandle["recipeId"] ?: 0
    private val _recipeLiveData = MutableLiveData<RecipeState>()
    val recipeLiveData: LiveData<RecipeState>
        get() = _recipeLiveData

    private var favoritesSet = mutableSetOf<Int>()

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
