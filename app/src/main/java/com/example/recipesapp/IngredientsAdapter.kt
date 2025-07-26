package com.example.recipesapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity: Int = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemIngredientBinding = ItemIngredientBinding.bind(view)
        val tvIngredientName = itemIngredientBinding.tvIngredientName
        val tvQuantityIngredients = itemIngredientBinding.tvIngredientQuantity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        val quantityIngredients =
            "${makeCorrectQuantity(ingredient.quantity)} ${ingredient.unitOfMeasure}"
        with(holder) {
            tvIngredientName.text = ingredient.description
            tvQuantityIngredients.text = quantityIngredients
        }
    }

    override fun getItemCount(): Int = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    private fun makeCorrectQuantity(quantityPerOne: String): String {
        val quantityToDouble = quantityPerOne.toDouble() * quantity
        val finalQuantity =
            if (quantityToDouble % 1.0 == 0.0) quantityToDouble.toInt()
            else quantityToDouble
        return finalQuantity.toString()
    }
}
