package com.example.recipesapp.ui.recipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Ingredient

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val id: Int? = null,
        val title: String? = null,
        val ingredients: List<Ingredient> = listOf(),
        val method: List<String> = listOf(),
        val imageUrl: String? = null,
        var isFavorite: Boolean = false,
    )

    private val _recipeLiveData = MutableLiveData<RecipeState>()
    val recipeLiveData: LiveData<RecipeState>
        get() = _recipeLiveData

    init {
        Log.i("!!!", "ViewModel created")
        _recipeLiveData.value = RecipeState(isFavorite = true)
    }

    fun setNewState(state: RecipeState) {
        _recipeLiveData.value = state
    }
}