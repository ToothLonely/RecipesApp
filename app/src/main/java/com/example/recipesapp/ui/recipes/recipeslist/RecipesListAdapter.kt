package com.example.recipesapp.ui.recipes.recipeslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.data.IMAGE_URL
import com.example.recipesapp.databinding.ItemRecipeBinding
import com.example.recipesapp.model.Recipe

class RecipesListAdapter(private var dataSet: List<Recipe>, fragment: Fragment) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    private val glide = Glide.with(fragment)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemRecipeBinding = ItemRecipeBinding.bind(view)
        val tvRecipeName = itemRecipeBinding.tvRecipeName
        val ivBcgRecipe = itemRecipeBinding.ivBcgRecipe
        val cvRecipe = itemRecipeBinding.cvRecipe
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = dataSet[position]

        with(holder) {
            tvRecipeName.text = recipe.title

            glide.load("$IMAGE_URL${recipe.imageUrl}")
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivBcgRecipe)

            cvRecipe.setOnClickListener {
                itemClickListener?.onItemClick(recipe.id)
            }
        }
    }

    override fun getItemCount() = dataSet.size

    fun setNewDataSet(newDataSet: List<Recipe>) {
        dataSet = newDataSet
        notifyDataSetChanged()
    }
}