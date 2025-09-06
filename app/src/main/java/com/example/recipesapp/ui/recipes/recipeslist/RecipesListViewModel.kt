package com.example.recipesapp.ui.recipes.recipeslist

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe

class RecipesListViewModel(
    private val category: Category,
    application: Application,
) : AndroidViewModel(application) {

    private val _recipesListLiveData = MutableLiveData<RecipesListState>()
    val recipesListLiveData: LiveData<RecipesListState>
        get() = _recipesListLiveData

    data class RecipesListState(
        val title: String? = null,
        val image: Drawable? = null,
        val dataSet: List<Recipe> = listOf()
    )

    init {
        loadRecipesList(application)
    }

    private fun loadRecipesList(application: Application) {

        val categoryImage = try {
            Drawable.createFromStream(
                application.assets.open(category.imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.e("RecipesListViewModel", "Cannot create drawable from assets")
            null
        }

        _recipesListLiveData.value = RecipesListState(
            category.title,
            categoryImage,
            STUB.getRecipesByCategoryId(category.id),
        )
    }

    fun openRecipeByRecipeId(fragment: Fragment, recipeId: Int) {

        val action = RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)

        fragment.findNavController().navigate(action)
    }

}