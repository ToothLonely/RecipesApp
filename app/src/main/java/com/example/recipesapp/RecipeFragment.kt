package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.content.res.AppCompatResources.getDrawable
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
    private val portionString
        get() = requireContext().getString(R.string.tv_portion)

    private var isFavorite = true

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

        val drawable = try {
            Drawable.createFromStream(
                view?.context?.assets?.open(recipe.imageUrl),
                null
            )
        } catch (e: Exception) {
            throw java.lang.IllegalStateException("Cannot create drawable")
        }

        with(recipeFragmentBinding) {
            tvRecipeTitle.text = recipe.title
            ivRecipeBcg.setImageDrawable(drawable)
            tvPortion.text = portionString
            tvNumberOfPortions.text = DEFAULT_NUMBER_OF_PORTIONS.toString()
            ibFavorites.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.ic_heart_empty_big
                )
            )
            ibFavorites.setOnClickListener {
                if (isFavorite) {
                    ibFavorites.setImageDrawable(
                        getDrawable(
                            requireContext(),
                            R.drawable.ic_heart_big
                        )
                    )
                } else {
                    ibFavorites.setImageDrawable(
                        getDrawable(
                            requireContext(),
                            R.drawable.ic_heart_empty_big
                        )
                    )
                }
                isFavorite = !isFavorite
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

}