package com.example.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.ui.recipes.FavoritesViewModel
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _fragmentsFavoritesBinding: FragmentFavoritesBinding? = null
    private val fragmentFavoritesBinding
        get() = _fragmentsFavoritesBinding ?: throw IllegalStateException(
            "Binding for FavoritesFragmentBinding mustn't be null"
        )

    private val viewModel: FavoritesViewModel by activityViewModels()

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

        val favoritesState = viewModel.favoritesLiveData.value
        val favoritesListAdapter =
            RecipesListAdapter(favoritesState?.favoritesLayoutState?.dataSet ?: emptyList())

        if (favoritesState?.isVisible == false) {
            with(fragmentFavoritesBinding) {
                tvDefaultFavorites.visibility = View.VISIBLE
                layoutFavorites.visibility = View.GONE
            }
        } else {
            with(fragmentFavoritesBinding) {
                tvDefaultFavorites.visibility = View.GONE
                layoutFavorites.visibility = View.VISIBLE
                rvFavorites.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = favoritesListAdapter
                }

                favoritesListAdapter.setOnItemClickListener(object :
                    RecipesListAdapter.OnItemClickListener {
                    override fun onItemClick(recipeId: Int) {
                        viewModel.openRecipeByRecipeId(this@FavoritesFragment, recipeId)
                    }
                })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.reloadFavorites()
    }
}