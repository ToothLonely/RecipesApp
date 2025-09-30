package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.data.FAVORITES
import com.example.recipesapp.data.FAVORITES_SET
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _favoritesLiveData = MutableLiveData<FavoritesState>()
    val favoritesLiveData: LiveData<FavoritesState>
        get() = _favoritesLiveData

    private val sharedPrefs = application.getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)

    data class FavoritesState(
        val isVisible: Boolean? = null,
        val favoritesLayoutState: FavoritesLayoutState? = null
    )

    data class FavoritesLayoutState(
        val title: String? = null,
        val image: Drawable? = null,
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
        val isVisible = getFavorites().isNotEmpty()

        _favoritesLiveData.value = FavoritesState(
            isVisible,
            if (isVisible) FavoritesLayoutState(
                title = application.getString(R.string.tv_favorites),
                image = getDrawable(application, R.drawable.bcg_favorites),
                dataSet = getRecipesByIds(getFavorites()),
            ) else null,
        )
    }

    private suspend fun getRecipesByIds(set: Set<String>): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            RecipesRepository.getRecipesByIds(set)
        }
    }

    private fun getFavorites(): MutableSet<String> {
        return sharedPrefs.getStringSet(FAVORITES_SET, mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()
    }

    fun openRecipeByRecipeId(fragment: Fragment, recipeId: Int) {

        val action = FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipeId)

        fragment.findNavController().navigate(action)
    }
}
