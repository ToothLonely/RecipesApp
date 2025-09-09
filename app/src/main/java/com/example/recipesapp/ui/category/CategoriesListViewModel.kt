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
import com.example.recipesapp.data.STUB
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
        _categoriesListLiveData.value = CategoriesListState(
            getString(application, R.string.tv_categories),
            getDrawable(application, R.drawable.bcg_categories),
            STUB.getCategories()
        )
    }

    fun openRecipesByCategoryId(fragment: Fragment, categoryId: Int) {
        val currentCategory: Category = STUB.getCategories().find { it.id == categoryId }
            ?: throw IllegalArgumentException("Категория с ID $categoryId не найдена!")

        val action = CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(currentCategory)

        fragment.findNavController().navigate(action)
    }

}