package com.example.recipesapp.ui.category

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    @param:ApplicationContext private val application: Context,
    private val repo: RecipesRepository,
) : ViewModel() {

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
            loadCategoryList()
        }
    }

    private suspend fun loadCategoryList() {

        val cachedCategories = repo.getCategoriesFromCache()
        updateUI(cachedCategories)

        val backedCategories = repo.getCategories()
        if (backedCategories != null) {
            updateUI(backedCategories)
            repo.addCategories(backedCategories)
        }
    }

    private fun updateUI(categories: List<Category>) {
        _categoriesListLiveData.value = CategoriesListState(
            categoryTitle = getString(application, R.string.tv_categories),
            dataSet = categories
        )
    }

    fun openRecipesByCategoryId(fragment: Fragment, categoryId: Int) {
        viewModelScope.launch {
            val currentCategory: Category =
                repo.getCategoriesFromCache().find { it.id == categoryId }
                    ?: throw IllegalArgumentException("Категория с ID $categoryId не найдена!")

            val action =
                CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                    currentCategory
                )

            fragment.findNavController().navigate(action)
        }
    }
}
