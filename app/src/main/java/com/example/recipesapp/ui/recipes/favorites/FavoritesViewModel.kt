package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.data.ARG_RECIPE
import com.example.recipesapp.data.FAVORITES
import com.example.recipesapp.data.FAVORITES_SET
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment

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
        val dataSet: List<Recipe> = listOf()
    )

    init {
        loadFavorites(application)
    }

    fun reloadFavorites(){
        loadFavorites(getApplication())
    }

    private fun loadFavorites(application: Application) {
        val isVisible = getFavorites().isNotEmpty()

        _favoritesLiveData.value = FavoritesState(
            isVisible,
            if (isVisible) FavoritesLayoutState(
                application.getString(R.string.tv_favorites),
                getDrawable(application, R.drawable.bcg_favorites),
                STUB.getRecipesByIds(getFavorites()),
            ) else null,
        )
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