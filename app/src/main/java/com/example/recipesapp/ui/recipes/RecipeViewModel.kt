package com.example.recipesapp.ui.recipes

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.DEFAULT_NUMBER_OF_PORTIONS
import com.example.recipesapp.data.FAVORITES
import com.example.recipesapp.data.FAVORITES_SET
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.toRecipeState

class RecipeViewModel(private val recipeId: Int, application: Application) :
    AndroidViewModel(application) {

    private val _recipeLiveData = MutableLiveData<RecipeState>()
    val recipeLiveData: LiveData<RecipeState>
        get() = _recipeLiveData

    private val sharedPrefs = application.getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)

    private val favoritesSet = getFavorites()

    data class RecipeState(
        val id: Int? = null,
        val title: String? = null,
        val ingredients: List<Ingredient> = listOf(),
        val method: List<String> = listOf(),
        val imageUrl: String? = null,
        var isFavorite: Boolean? = null,
        var portionsCount: Int? = null,
    )

    init {
        Log.i("!!!", "ViewModel created")
        loadRecipe(recipeId)
    }

    private fun loadRecipe(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        Log.i("!!!", "recipeId indide VM: $recipeId")
        _recipeLiveData.value = recipe.toRecipeState()
        _recipeLiveData.value?.apply {
            isFavorite = id.toString() in favoritesSet
            Log.i("!!!", "favoritesSet in VM $favoritesSet isFavorite $isFavorite")
            portionsCount = DEFAULT_NUMBER_OF_PORTIONS
        }
        Log.i("!!!", "Loaded recipe: ${_recipeLiveData.value!!.title}")
    }

    private fun getFavorites(): MutableSet<String> {
        return sharedPrefs.getStringSet(FAVORITES_SET, mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()
    }

    private fun saveFavorites(favoritesSet: Set<String>) {
        sharedPrefs.edit {
            putStringSet(FAVORITES_SET, favoritesSet)
        }
    }

    fun onFavoritesClicked() {
        val favoriteFlag: Boolean

        if (_recipeLiveData.value?.isFavorite == true) {
            favoriteFlag = false
            favoritesSet.remove(recipeId.toString())
        } else {
            favoriteFlag = true
            favoritesSet.add(recipeId.toString())
        }

        saveFavorites(favoritesSet)
        _recipeLiveData.value = _recipeLiveData.value?.copy(isFavorite = favoriteFlag)
    }

}