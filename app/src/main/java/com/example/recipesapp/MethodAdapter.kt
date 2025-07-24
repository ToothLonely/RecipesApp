package com.example.recipesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemMethodBinding

class MethodAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvMethodDescription = ItemMethodBinding.bind(view).tvMethodDescription
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
