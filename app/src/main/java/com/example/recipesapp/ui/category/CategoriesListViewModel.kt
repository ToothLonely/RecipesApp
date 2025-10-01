package com.example.recipesapp.ui.category

import android.app.Application
import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.data.BASE_URL
import com.example.recipesapp.data.IMAGE_URL
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _categoriesListLiveData = MutableLiveData<CategoriesListState>()
    val categoriesListLiveData: LiveData<CategoriesListState>
        get() = _categoriesListLiveData

    data class CategoriesListState(
        val categoryTitle: String? = null,
        val categoryImageRef: String? = null,
        val dataSet: List<Category>? = listOf(),
    )

    init {
        viewModelScope.launch {
            loadCategoryList(application)
        }
    }

    private suspend fun loadCategoryList(application: Application) {
        _categoriesListLiveData.value = CategoriesListState(
            categoryTitle = getString(application, R.string.tv_categories),
            dataSet = getCategories()
        )
    }

    private suspend fun getCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            RecipesRepository.getCategories()
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
