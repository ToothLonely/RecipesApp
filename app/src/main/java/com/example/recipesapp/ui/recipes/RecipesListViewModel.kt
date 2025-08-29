package com.example.recipesapp.ui.recipes

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.R
import com.example.recipesapp.data.ARG_RECIPE
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment

class RecipesListViewModel(
    private val categoryId: Int,
    private val categoryName: String?,
    private val categoryImageUrl: String?,
    application: Application,
) : AndroidViewModel(application) {

    private val _recipesListLiveData = MutableLiveData<RecipesListState>()
    val recipesListLiveData
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
                categoryImageUrl?.let { application.assets.open(it) },
                null
            )
        } catch (e: Exception) {
            Log.e("RecipesListViewModel", "Cannot create drawable from assets")
            null
        }

        _recipesListLiveData.value = RecipesListState(
            categoryName,
            categoryImage,
            STUB.getRecipesByCategoryId(categoryId),
        )
    }

    fun openRecipeByRecipeId(fragment: Fragment, recipeId: Int) {

        val bundle = bundleOf(
            ARG_RECIPE to recipeId
        )

        fragment.parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

}