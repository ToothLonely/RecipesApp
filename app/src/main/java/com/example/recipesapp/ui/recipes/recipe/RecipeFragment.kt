package com.example.recipesapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.recipesapp.data.DEFAULT_NUMBER_OF_PORTIONS
import com.example.recipesapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private var _recipeFragmentBinding: FragmentRecipeBinding? = null
    private val recipeFragmentBinding: FragmentRecipeBinding
        get() = _recipeFragmentBinding ?: throw IllegalStateException(
            "Binding for recipeFragmentBinding mustn't be null"
        )

    private val portionString
        get() = requireContext().getString(R.string.tv_portion)

    private val viewModel: RecipeViewModel by viewModels()

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

            ingredientsAdapter.setNewDataSet(it.ingredients)
            methodAdapter.setNewDataSet(it.method)

            if (it == null) {
                Toast.makeText(
                    context,
                    getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }

            val icon =
                if (it.isFavorite == true) R.drawable.ic_heart_big
                else R.drawable.ic_heart_empty_big

            with(recipeFragmentBinding) {
                tvRecipeTitle.text = it.title
                tvPortion.text = portionString
                tvNumberOfPortions.text = it.portionsCount.toString()

                Glide.with(requireContext())
                    .load(it.recipeImage)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(ivRecipeBcg)

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