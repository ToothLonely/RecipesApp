package com.example.recipesapp.ui.recipe.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.data.ARG_RECIPE
import com.example.recipesapp.data.FAVORITES
import com.example.recipesapp.data.FAVORITES_SET
import com.example.recipesapp.R
import com.example.recipesapp.data.STUB
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.ui.recipe.recipe.RecipeFragment
import com.example.recipesapp.ui.recipe.recipeslist.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _fragmentsFavoritesBinding: FragmentFavoritesBinding? = null
    private val fragmentFavoritesBinding
        get() = _fragmentsFavoritesBinding ?: throw IllegalStateException(
            "Binding for FavoritesFragmentBinding mustn't be null"
        )

    private val sharedPrefs
        get() = requireContext().getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentsFavoritesBinding =
            FragmentFavoritesBinding.inflate(inflater, container, false)
        return fragmentFavoritesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFavorites()
    }

    private fun initFavorites() {
        if (getFavorites().isEmpty()) {
            with(fragmentFavoritesBinding) {
                tvDefaultFavorites.visibility = View.VISIBLE
                layoutFavorites.visibility = View.GONE
            }
        } else {
            with(fragmentFavoritesBinding) {
                tvDefaultFavorites.visibility = View.GONE
                layoutFavorites.visibility = View.VISIBLE
            }
            initRecycler()
        }
    }

    private fun initRecycler() {
        val listOfRecipes = STUB.getRecipesByIds(getFavorites())
        val favoritesListAdapter = RecipesListAdapter(listOfRecipes)

        fragmentFavoritesBinding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritesListAdapter
        }

        favoritesListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun getFavorites(): MutableSet<String> {
        return sharedPrefs.getStringSet(FAVORITES_SET, mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val recipe = STUB.getRecipeById(recipeId)
        val bundle = bundleOf(
            ARG_RECIPE to recipe
        )

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}