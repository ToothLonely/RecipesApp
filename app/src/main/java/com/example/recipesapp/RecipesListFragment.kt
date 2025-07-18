package com.example.recipesapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class RecipesListFragment : Fragment() {
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategory()
    }

    private fun initCategory() {
        categoryId = requireArguments().getInt(ARG_CATEGORY_ID)
        categoryName = requireArguments().getString(ARG_CATEGORY_NAME)
        categoryImageUrl = requireArguments().getString(ARG_CATEGORY_IMAGE_URL)
    }
}