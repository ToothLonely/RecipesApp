package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipesapp.databinding.FragmentListCategoriesBinding

const val ARG_CATEGORY_ID = "categoryId"
const val ARG_CATEGORY_NAME = "categoryName"
const val ARG_CATEGORY_IMAGE_URL = "categoryImageUrl"

class CategoriesListFragment : Fragment() {
    private var _categoriesListFragmentBinding: FragmentListCategoriesBinding? = null
    private val categoriesListFragmentBinding
        get() = _categoriesListFragmentBinding ?: throw IllegalStateException(
            "Binding for CategoriesListFragmentBinding mustn't be null"
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _categoriesListFragmentBinding =
            FragmentListCategoriesBinding.inflate(inflater, container, false)
        return categoriesListFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        val categoriesAdapter = CategoriesListAdapter(STUB.getCategories())

        categoriesListFragmentBinding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = categoriesAdapter
        }

        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val currentCategory: Category = STUB.getCategories().find { it.id == categoryId }
            ?: throw IllegalArgumentException("Категория с ID $categoryId не найдена!")
        val (_, categoryName, _, categoryImageUrl) = currentCategory

        val bundle: Bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl
        )

        parentFragmentManager.commit {
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}