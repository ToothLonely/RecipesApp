package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentListCategoriesBinding

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
}