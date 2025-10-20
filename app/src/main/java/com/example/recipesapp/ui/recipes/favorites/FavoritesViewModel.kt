package com.example.recipesapp.ui.recipes.favorites

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    @param:ApplicationContext private val application: Context,
    private val repo: RecipesRepository,
) : ViewModel() {

    private val _favoritesLiveData = MutableLiveData<FavoritesState>()
    val favoritesLiveData: LiveData<FavoritesState>
        get() = _favoritesLiveData

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
            loadFavorites()
        }
    }

    fun reloadFavorites() {
        viewModelScope.launch {
            loadFavorites()
        }
    }

    private suspend fun loadFavorites() {
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
