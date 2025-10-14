package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _favoritesLiveData = MutableLiveData<FavoritesState>()
    val favoritesLiveData: LiveData<FavoritesState>
        get() = _favoritesLiveData

    private val repo = RecipesRepository(application)

    data class FavoritesState(
        val isVisible: Boolean? = null,
        val favoritesLayoutState: FavoritesLayoutState? = null
    )

    data class FavoritesLayoutState(
        val title: String? = null,
        val dataSet: List<Recipe>? = listOf()
    )

    init {
        viewModelScope.launch {
            loadFavorites(application)
        }
    }

    fun reloadFavorites() {
        viewModelScope.launch {
            loadFavorites(getApplication())
        }
    }

    private suspend fun loadFavorites(application: Application) {
        val favoritesSet = getFavorites()
        val isVisible = favoritesSet.isNotEmpty()

        _favoritesLiveData.value = FavoritesState(
            isVisible,
            if (isVisible) FavoritesLayoutState(
                title = application.getString(R.string.tv_favorites),
                dataSet = getRecipesByIds(favoritesSet),
            ) else null,
        )
    }

    private suspend fun getRecipesByIds(set: Set<Int>): List<Recipe>? {
        return repo.getRecipesByIds(set)
    }

    private suspend fun getFavorites(): MutableSet<Int> {
        return repo.getFavoritesSet()
    }

    fun openRecipeByRecipeId(fragment: Fragment, recipeId: Int) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipeId)
        fragment.findNavController().navigate(action)
    }
}
