package com.example.recipesapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
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
        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(ARG_RECIPE, Recipe::class.java)
                ?: throw IllegalArgumentException(
                    "Couldn't transmit arguments from RecipesListFragment"
                )
        } else {
            requireArguments().getParcelable<Recipe>(ARG_RECIPE) ?: throw IllegalArgumentException(
                "Couldn't transmit arguments from RecipesListFragment"
            )
        }
        val recipeId = recipe.id.toString()

        val drawable = try {
            Drawable.createFromStream(
                view?.context?.assets?.open(recipe.imageUrl),
                null
            )
        } catch (e: Exception) {
            throw java.lang.IllegalStateException("Cannot create drawable")
        }

        val favoritesSet = getFavorites()
        var icon =
            if (recipeId in favoritesSet) R.drawable.ic_heart_big
            else R.drawable.ic_heart_empty_big

        with(recipeFragmentBinding) {
            tvRecipeTitle.text = recipe.title
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

                icon = if (recipeId in favoritesSet) {
                    favoritesSet.remove(recipeId)
                    R.drawable.ic_heart_empty_big
                } else {
                    favoritesSet.add(recipeId)
                    R.drawable.ic_heart_big
                }
                ibFavorites.setImageDrawable(getDrawable(requireContext(), icon))

                saveFavorites(favoritesSet)
            }
        }

        initRecyclers(recipe.ingredients, recipe.method)
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
        val set = sharedPrefs.getStringSet(FAVORITES_SET, mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()
        return set
    }

}