package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.databinding.FragmentRecipesListBinding
import java.lang.IllegalStateException

class RecipesListFragment : Fragment() {
    private var _recipesListFragmentBinding: FragmentRecipesListBinding? = null
    private val recipesListFragmentBinding: FragmentRecipesListBinding
        get() = _recipesListFragmentBinding ?: throw IllegalStateException(
            "Binding for recipesListFragmentBinding mustn't be null"
        )

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

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
        initRecipesRecycler()
    }

    private fun initCategory(): Int? {

        categoryId = requireArguments().getInt(ARG_CATEGORY_ID)
        categoryName = requireArguments().getString(ARG_CATEGORY_NAME)
        categoryImageUrl = requireArguments().getString(ARG_CATEGORY_IMAGE_URL)

        val drawable = try {
            Drawable.createFromStream(
                categoryImageUrl?.let { view?.context?.assets?.open(it) },
                null
            )
        } catch (e: Exception) {
            throw IllegalStateException("cannot create drawable")
        }
        recipesListFragmentBinding.tvCategoryName.text = categoryName
        recipesListFragmentBinding.ivCategoryBck.background = drawable

        return categoryId
    }

    private fun initRecipesRecycler() {
        val recipesListAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId))

        recipesListFragmentBinding.rvRecipes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipesListAdapter
        }
    }
}