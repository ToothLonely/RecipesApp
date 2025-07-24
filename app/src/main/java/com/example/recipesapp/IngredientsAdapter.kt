package com.example.recipesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemIngredientBinding = ItemIngredientBinding.bind(view)
        var tvIngredientName = itemIngredientBinding.tvIngredientName
        var tvQuantityIngredients = itemIngredientBinding.tvIngredientQuantity

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        val quantityIngredients = "${ingredient.quantity} ${ingredient.unitOfMeasure}"
        with(holder) {
            tvIngredientName.text = ingredient.description
            tvQuantityIngredients.text = quantityIngredients
        }
    }

    override fun getItemCount(): Int = dataSet.size
}
