package com.example.recipesapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.data.ARG_CATEGORY_ID
import com.example.recipesapp.data.ARG_CATEGORY_IMAGE_URL
import com.example.recipesapp.data.ARG_CATEGORY_NAME
import com.example.recipesapp.databinding.FragmentRecipesListBinding
import com.example.recipesapp.ui.recipes.RecipesListViewModel
import com.example.recipesapp.ui.recipes.RecipesListViewModelFactory
import java.lang.IllegalStateException

class RecipesListFragment : Fragment() {
    private var _recipesListFragmentBinding: FragmentRecipesListBinding? = null
    private val recipesListFragmentBinding: FragmentRecipesListBinding
        get() = _recipesListFragmentBinding ?: throw IllegalStateException(
            "Binding for recipesListFragmentBinding mustn't be null"
        )

    private val application
        get() = requireActivity().application

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            RecipesListViewModelFactory(
                requireArguments().getInt(ARG_CATEGORY_ID),
                requireArguments().getString(ARG_CATEGORY_NAME),
                requireArguments().getString(ARG_CATEGORY_IMAGE_URL),
                application
            )
        )[RecipesListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _recipesListFragmentBinding =
            FragmentRecipesListBinding.inflate(inflater, container, false)
        return recipesListFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategory()
    }

    private fun initCategory() {
        viewModel.recipesListLiveData.observe(viewLifecycleOwner, Observer {

            val recipesListAdapter = RecipesListAdapter(it?.dataSet ?: emptyList())

            with(recipesListFragmentBinding) {
                tvCategoryName.text = it?.title
                ivCategoryBck.setImageDrawable(it?.image)
                rvRecipes.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = recipesListAdapter
                }
            }

            recipesListAdapter.setOnItemClickListener(object :
                RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    viewModel.openRecipeByRecipeId(this@RecipesListFragment, recipeId)
                }
            })

        })


    }
}