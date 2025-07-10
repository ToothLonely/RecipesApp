package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemCategoryBinding
import java.io.InputStream

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemCategoryBinding = ItemCategoryBinding.bind(view)
        val tvCategoryName = itemCategoryBinding.tvCategoryName
        val ivCategoryHead = itemCategoryBinding.ivCategoryHead
        val tvCategoryDescription = itemCategoryBinding.tvCategoryDescription
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category = dataSet[position]
        val drawable = try {
            Drawable.createFromStream(
                viewHolder.itemView.context?.assets?.open(category.imageUrl),
                null
            )
        } catch (e: Exception) {
            val stackTrace = Log.getStackTraceString(e)
            Log.e("OnBindViewHolder", stackTrace)
            null
        }
        with(viewHolder){
            tvCategoryName.text = category.title
            tvCategoryDescription.text = category.description
            ivCategoryHead.setImageDrawable(drawable)
        }
    }

    override fun getItemCount() = dataSet.size

}