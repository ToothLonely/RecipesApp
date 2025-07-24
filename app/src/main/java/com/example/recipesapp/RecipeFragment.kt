package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {

    private var _recipeFragmentBinding: FragmentRecipeBinding? = null
    private val recipeFragmentBinding: FragmentRecipeBinding
        get() = _recipeFragmentBinding ?: throw IllegalStateException(
            "Binding for recipeFragmentBinding mustn't be null"
        )

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

        val drawable = Drawable.createFromStream(
            view?.context?.assets?.open(recipe.imageUrl),
            null
        )
        with(recipeFragmentBinding) {
            tvRecipeTitle.text = recipe.title
            ivRecipeBcg.setImageDrawable(drawable)
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
        }

        recipeFragmentBinding.rvIngredients.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = IngredientsAdapter(ingredients)
            addItemDecoration(divider)
        }

        recipeFragmentBinding.rvMethods.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = MethodAdapter(method)
            addItemDecoration(divider)
        }
    }

}