package com.example.recipesapp.ui.category

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.data.RepositoryCallback
import com.example.recipesapp.data.Result
import com.example.recipesapp.model.Category

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _categoriesListLiveData = MutableLiveData<CategoriesListState>()
    val categoriesListLiveData: LiveData<CategoriesListState>
        get() = _categoriesListLiveData

    data class CategoriesListState(
        val categoryTitle: String? = null,
        val categoryImageBackground: Drawable? = null,
        val dataSet: List<Category> = listOf(),
    )

    init {
        loadCategoryList(application)
    }

    private fun loadCategoryList(application: Application) {
        val categories = listOf<Category>()
        RecipesRepository.getCategories(object :
            RepositoryCallback {
            override fun onComplete(result: Result, categories: List<Category>): List<Category> {
                return if (result is Result.Success<*>) {
                    categories
                } else {
                    emptyList()
                }
            }
        })
        _categoriesListLiveData.value = CategoriesListState(
            getString(application, R.string.tv_categories),
            categoryImageBackground = getDrawable(application, R.drawable.bcg_categories),
            dataSet =
        )
    }

    fun openRecipesByCategoryId(fragment: Fragment, categoryId: Int) {
        val currentCategory: Category =
            RecipesRepository.getCategories().find { it.id == categoryId }
                ?: throw IllegalArgumentException("Категория с ID $categoryId не найдена!")

        val action =
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                currentCategory
            )

        fragment.findNavController().navigate(action)
    }

}