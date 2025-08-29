package com.example.recipesapp.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _categoriesListFragmentBinding: FragmentListCategoriesBinding? = null
    private val categoriesListFragmentBinding
        get() = _categoriesListFragmentBinding ?: throw IllegalStateException(
            "Binding for CategoriesListFragmentBinding mustn't be null"
        )

    private val viewModel: CategoriesListViewModel by activityViewModels()

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
        initUI()
    }

    private fun initUI() {
        val categoriesListState = viewModel.categoriesListLiveData.value
        val categoriesAdapter = CategoriesListAdapter(categoriesListState?.dataSet ?: emptyList())

        with(categoriesListFragmentBinding) {
            tvCategories.text = categoriesListState?.categoryTitle
            ivBckCategories.background = categoriesListState?.categoryImageBackground
            rvCategories.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = categoriesAdapter
            }
        }

        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                viewModel.openRecipesByCategoryId(this@CategoriesListFragment, categoryId)
            }
        })
    }
}