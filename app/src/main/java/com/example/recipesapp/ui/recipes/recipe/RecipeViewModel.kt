package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        var recipeImage: Drawable? = null,
    )

    init {
        loadRecipe(recipeId)
    }

    private fun loadRecipe(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        _recipeLiveData.value = recipe.toRecipeState()
        _recipeLiveData.value?.apply {
            isFavorite = id.toString() in favoritesSet
            portionsCount = DEFAULT_NUMBER_OF_PORTIONS
            recipeImage = try {
                Drawable.createFromStream(
                    imageUrl?.let {
                        getApplication<Application>().applicationContext?.assets?.open(
                            it
                        )
                    },
                    null
                )
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Cannot create drawable from assets")
                null
            }
        }
    }

    private fun getFavorites(): MutableSet<String> {
        return sharedPrefs.getStringSet(FAVORITES_SET, mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()
    }

    fun saveFavorites() {
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

        _recipeLiveData.value = _recipeLiveData.value?.copy(isFavorite = favoriteFlag)
    }

    fun setPortionsCount(newPortionsCount: Int) {
        _recipeLiveData.value = _recipeLiveData.value?.copy(portionsCount = newPortionsCount)
    }

}