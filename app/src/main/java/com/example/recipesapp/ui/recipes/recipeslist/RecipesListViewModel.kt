package com.example.recipesapp.ui.recipes.recipeslist

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipesListViewModel(
    private val category: Category,
    application: Application,
) : AndroidViewModel(application) {

    private val _recipesListLiveData = MutableLiveData<RecipesListState>()
    val recipesListLiveData: LiveData<RecipesListState>
        get() = _recipesListLiveData

    private val recipesRepo = RecipesRepository()

    data class RecipesListState(
        val title: String? = null,
        val image: Drawable? = null,
        val dataSet: List<Recipe>? = listOf()
    )

    init {
        viewModelScope.launch {
            loadRecipesList(application)
        }
    }

    private suspend fun loadRecipesList(application: Application) {

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
            title = category.title,
            image = categoryImage,
            dataSet = getRecipesByCategoryId(category.id),
        )
    }

    private suspend fun getRecipesByCategoryId(categoryId: Int?): List<Recipe>? {
        val category = withContext(Dispatchers.IO) {
            recipesRepo.getRecipesByCategoryId(categoryId)
        }

        return when (category) {
            null -> {
                Toast.makeText(
                    application,
                    getString(application, R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
                null
            }

            else -> category
        }
    }

    fun openRecipeByRecipeId(fragment: Fragment, recipeId: Int) {

        val action =
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)

        fragment.findNavController().navigate(action)
    }
}
