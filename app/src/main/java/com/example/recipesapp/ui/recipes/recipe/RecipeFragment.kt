package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.core.content.edit
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.data.ARG_RECIPE
import com.example.recipesapp.data.DEFAULT_NUMBER_OF_PORTIONS
import com.example.recipesapp.data.FAVORITES
import com.example.recipesapp.data.FAVORITES_SET
import com.example.recipesapp.R
import com.example.recipesapp.data.DEFAULT_RECIPE_IMAGE_URL
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.ui.recipes.RecipeViewModel
import com.example.recipesapp.ui.recipes.RecipeViewModelFactory

class RecipeFragment : Fragment() {

    private var _recipeFragmentBinding: FragmentRecipeBinding? = null
    private val recipeFragmentBinding: FragmentRecipeBinding
        get() = _recipeFragmentBinding ?: throw IllegalStateException(
            "Binding for recipeFragmentBinding mustn't be null"
        )

    private val portionString
        get() = requireContext().getString(R.string.tv_portion)

    private val sharedPrefs
        get() = requireContext().getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)

    private val application
        get() = requireActivity().application

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            RecipeViewModelFactory(requireArguments().getInt(ARG_RECIPE), application)
        )[RecipeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _recipeFragmentBinding = FragmentRecipeBinding.inflate(inflater, container, false)
        return recipeFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {

        viewModel.recipeLiveData.observe(viewLifecycleOwner, Observer {
            val recipeId = it.id
            Log.i("!!!", "recipeId from bundle: $recipeId")

            val drawable = try {
                val imageUrl = it.imageUrl ?: DEFAULT_RECIPE_IMAGE_URL
                Drawable.createFromStream(
                    view?.context?.assets?.open(imageUrl),
                    null
                )
            } catch (e: Exception) {
                throw java.lang.IllegalStateException("Cannot create drawable")
            }

            val favoritesSet = getFavorites()
            Log.i("!!!", "favoritesSet in start $favoritesSet and isFavorite: ${it.isFavorite}")
            val icon =
                if (it.isFavorite == true) R.drawable.ic_heart_big
                else R.drawable.ic_heart_empty_big

            with(recipeFragmentBinding) {
                tvRecipeTitle.text = it.title
                ivRecipeBcg.setImageDrawable(drawable)
                tvPortion.text = portionString
                tvNumberOfPortions.text = DEFAULT_NUMBER_OF_PORTIONS.toString()
                ibFavorites.setImageDrawable(
                    getDrawable(
                        requireContext(),
                        icon
                    )
                )
                ibFavorites.setOnClickListener {
                    Log.i("!!!", "favorites clicked $favoritesSet")

                    val newState = viewModel.recipeLiveData.value?.copy()
                        ?: throw IllegalStateException("cannot copy current state")

                    if (viewModel.recipeLiveData.value?.isFavorite!!) {
                        newState.isFavorite = false
                        favoritesSet.remove(recipeId.toString())
                        ibFavorites.setImageDrawable(
                            getDrawable(
                                requireContext(),
                                R.drawable.ic_heart_empty_big
                            )
                        )
                    } else {
                        newState.isFavorite = true
                        favoritesSet.add(recipeId.toString())
                        ibFavorites.setImageDrawable(
                            getDrawable(
                                requireContext(),
                                R.drawable.ic_heart_big
                            )
                        )
                    }
                    Log.i("!!!", "new state favorite is: ${newState.isFavorite}")
                    saveFavorites(favoritesSet)
                    viewModel.setNewState(newState)
                }
            }

            initRecyclers(it.ingredients, it.method)

        })
    }

    private fun initRecyclers(ingredients: List<Ingredient>, method: List<String>) {
        val divider = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        ).apply {
            setDividerColorResource(requireContext(), R.color.dividerColor)
            isLastItemDecorated = false
            dividerInsetStart = resources.getDimension(R.dimen.halfMainDimen).toInt()
            dividerInsetEnd = resources.getDimension(R.dimen.halfMainDimen).toInt()
        }

        with(recipeFragmentBinding) {

            val ingredientsAdapter = IngredientsAdapter(ingredients)

            rvIngredients.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = ingredientsAdapter
                addItemDecoration(divider)
            }

            rvMethods.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = MethodAdapter(method)
                addItemDecoration(divider)
            }

            sbPortions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    ingredientsAdapter.updateIngredients(progress)
                    tvNumberOfPortions.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })
        }
    }

    private fun saveFavorites(favoritesSet: Set<String>) {
        sharedPrefs.edit {
            putStringSet(FAVORITES_SET, favoritesSet)
        }
    }

    private fun getFavorites(): MutableSet<String> {
        return sharedPrefs.getStringSet(FAVORITES_SET, mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()
    }

}