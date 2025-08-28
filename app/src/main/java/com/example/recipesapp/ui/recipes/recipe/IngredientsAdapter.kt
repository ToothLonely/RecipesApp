package com.example.recipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemIngredientBinding
import com.example.recipesapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(var dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity: Int = 1

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
        return BigDecimal(quantityPerOne)
            .multiply(BigDecimal(quantity))
            .setScale(1, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
    }
}
