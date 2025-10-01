package com.example.recipesapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.data.IMAGE_URL
import com.example.recipesapp.databinding.FragmentRecipesListBinding
import java.lang.IllegalStateException

class RecipesListFragment : Fragment() {
    private var _recipesListFragmentBinding: FragmentRecipesListBinding? = null
    private val recipesListFragmentBinding: FragmentRecipesListBinding
        get() = _recipesListFragmentBinding ?: throw IllegalStateException(
            "Binding for recipesListFragmentBinding mustn't be null"
        )

    private val application
        get() = requireActivity().application

    private val args: RecipesListFragmentArgs by navArgs()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            RecipesListViewModelFactory(
                args.category,
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
        val recipesListAdapter = RecipesListAdapter(
            viewModel.recipesListLiveData.value?.dataSet ?: emptyList(),
            this
        )

        recipesListFragmentBinding.rvRecipes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipesListAdapter
        }

        viewModel.recipesListLiveData.observe(viewLifecycleOwner, Observer {

            recipesListAdapter.setNewDataSet(it.dataSet ?: emptyList())

            if (it.dataSet == null) {
                Toast.makeText(
                    context,
                    getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }

            with(recipesListFragmentBinding) {
                tvCategoryName.text = it?.title

                Glide.with(requireContext())
                    .load(it.image)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(ivCategoryBck)
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