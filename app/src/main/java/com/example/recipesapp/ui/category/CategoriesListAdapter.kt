package com.example.recipesapp.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.data.IMAGE_URL
import com.example.recipesapp.databinding.ItemCategoryBinding
import com.example.recipesapp.model.Category

class CategoriesListAdapter(private var dataSet: List<Category>, private val fragment: Fragment) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private val glide = Glide.with(fragment)

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemCategoryBinding = ItemCategoryBinding.bind(view)
        val tvCategoryName = itemCategoryBinding.tvCategoryName
        val ivCategoryHead = itemCategoryBinding.ivCategoryHead
        val tvCategoryDescription = itemCategoryBinding.tvCategoryDescription
        val cvCategory = itemCategoryBinding.cvCategory
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category = dataSet[position]

        with(viewHolder) {
            tvCategoryName.text = category.title
            tvCategoryDescription.text = category.description

            glide.load("$IMAGE_URL${category.imageUrl}")
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivCategoryHead)

            cvCategory.setOnClickListener {
                val categoryId: Int = category.id
                itemClickListener?.onItemClick(categoryId)
            }
        }
    }

    override fun getItemCount() = dataSet.size

    fun setNewDataSet(newDataSet: List<Category>) {
        dataSet = newDataSet
        notifyDataSetChanged()
    }
}