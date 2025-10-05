package com.example.recipesapp.ui.recipes.recipeslist

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.data.IMAGE_URL
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

    private val repo = RecipesRepository(application)

    data class RecipesListState(
        val title: String? = null,
        val image: String? = null,
        val dataSet: List<Recipe>? = listOf()
    )

    init {
        viewModelScope.launch {
            loadRecipesList()
        }
    }

    private suspend fun loadRecipesList() {
        _recipesListLiveData.value = RecipesListState(
            title = category.title,
            image = "$IMAGE_URL${category.imageUrl}",
            dataSet = getRecipesByCategoryId(category.id),
        )
    }

    private suspend fun getRecipesByCategoryId(categoryId: Int?): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            repo.getRecipesByCategoryId(categoryId)
        }
    }

    fun openRecipeByRecipeId(fragment: Fragment, recipeId: Int) {

        val action =
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)

        fragment.findNavController().navigate(action)
    }
}
