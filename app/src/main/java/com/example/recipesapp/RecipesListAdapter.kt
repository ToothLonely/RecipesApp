package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemRecipeBinding

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemRecipeBinding = ItemRecipeBinding.bind(view)
        val tvRecipeName = itemRecipeBinding.tvRecipeName
        val ivBcgRecipe = itemRecipeBinding.ivBcgRecipe
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = dataSet[position]
        val drawable = try {
            Drawable.createFromStream(
                holder.itemView.context?.assets?.open(recipe.imageUrl),
                null
            )
        } catch (e: Exception) {
            val stackTrace = Log.getStackTraceString(e)
            Log.e("OnBindViewHolder", stackTrace)
            null
        }

        with(holder) {
            tvRecipeName.text = recipe.title
            ivBcgRecipe.background = drawable
        }
    }

    override fun getItemCount() = dataSet.size
}