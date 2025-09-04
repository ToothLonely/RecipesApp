package com.example.recipesapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.data.ARG_RECIPE
import com.example.recipesapp.data.DEFAULT_NUMBER_OF_PORTIONS
import com.example.recipesapp.R

class RecipeFragment : Fragment() {

    private var _recipeFragmentBinding: FragmentRecipeBinding? = null
    private val recipeFragmentBinding: FragmentRecipeBinding
        get() = _recipeFragmentBinding ?: throw IllegalStateException(
            "Binding for recipeFragmentBinding mustn't be null"
        )

    private val portionString
        get() = requireContext().getString(R.string.tv_portion)

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

        val divider = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        ).apply {
            setDividerColorResource(requireContext(), R.color.dividerColor)
            isLastItemDecorated = false
            dividerInsetStart = resources.getDimension(R.dimen.halfMainDimen).toInt()
            dividerInsetEnd = resources.getDimension(R.dimen.halfMainDimen).toInt()
        }

        val ingredientsAdapter =
            IngredientsAdapter(viewModel.recipeLiveData.value?.ingredients ?: emptyList())
        val methodAdapter = MethodAdapter(viewModel.recipeLiveData.value?.method ?: emptyList())

        with(recipeFragmentBinding) {
            rvIngredients.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = ingredientsAdapter
                addItemDecoration(divider)
            }

            rvMethods.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = methodAdapter
                addItemDecoration(divider)
            }
        }

        viewModel.recipeLiveData.observe(viewLifecycleOwner, Observer {

            val icon =
                if (it.isFavorite == true) R.drawable.ic_heart_big
                else R.drawable.ic_heart_empty_big

            with(recipeFragmentBinding) {
                tvRecipeTitle.text = it.title
                ivRecipeBcg.setImageDrawable(it.recipeImage)
                tvPortion.text = portionString
                tvNumberOfPortions.text = it.portionsCount.toString()

                ibFavorites.setImageDrawable(
                    getDrawable(
                        requireContext(),
                        icon
                    )
                )

                ibFavorites.setOnClickListener {
                    viewModel.onFavoritesClicked()
                }

                sbPortions.setOnSeekBarChangeListener(
                    PortionSeekBarListener { progress ->
                        viewModel.setPortionsCount(progress)
                    }
                )
            }

            ingredientsAdapter.updateIngredients(it.portionsCount ?: DEFAULT_NUMBER_OF_PORTIONS)

        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveFavorites()
    }
}