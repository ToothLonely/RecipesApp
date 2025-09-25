package com.example.recipesapp.ui.category

import android.app.Application
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _categoriesListLiveData = MutableLiveData<CategoriesListState>()
    val categoriesListLiveData: LiveData<CategoriesListState>
        get() = _categoriesListLiveData

    data class CategoriesListState(
        val categoryTitle: String? = null,
        val categoryImageBackground: Drawable? = null,
        val dataSet: List<Category>? = listOf(),
    )

    val recipeRepo = RecipesRepository()

    init {
        viewModelScope.launch {
            loadCategoryList(application)
        }
    }

    private suspend fun loadCategoryList(application: Application) {
        _categoriesListLiveData.value = CategoriesListState(
            categoryTitle = getString(application, R.string.tv_categories),
            categoryImageBackground = getDrawable(application, R.drawable.bcg_categories),
            dataSet = getCategories()
        )
    }

    private suspend fun getCategories(): List<Category>? {
        val categories = withContext(Dispatchers.IO) {
            recipeRepo.getCategories()
        }

        return when (categories) {
            null -> {
                Toast.makeText(
                    application,
                    getString(application, R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
                null
            }

            else -> categories
        }
    }

    fun openRecipesByCategoryId(fragment: Fragment, categoryId: Int) {
        viewModelScope.launch {
            val currentCategory: Category =
                getCategories()?.find { it.id == categoryId }
                    ?: throw IllegalArgumentException("Категория с ID $categoryId не найдена!")

            val action =
                CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                    currentCategory
                )

            fragment.findNavController().navigate(action)
        }
    }

}
