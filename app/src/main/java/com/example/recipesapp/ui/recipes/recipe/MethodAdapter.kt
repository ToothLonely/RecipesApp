package com.example.recipesapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemMethodBinding

class MethodAdapter(var dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMethodDescription = ItemMethodBinding.bind(view).tvMethodDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_method, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val method = dataSet[position]
        val methodDescription = "${position + 1}. $method"
        holder.tvMethodDescription.text = methodDescription
    }

    override fun getItemCount(): Int = dataSet.size
}
