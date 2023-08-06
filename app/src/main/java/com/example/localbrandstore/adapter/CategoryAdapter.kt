package com.example.localbrandstore.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.localbrandstore.R
import com.example.localbrandstore.activity.ProductByCategoryActivity
import com.example.localbrandstore.model.CategoryModel
import com.example.localbrandstore.service.ApiService
import com.squareup.picasso.Picasso

class CategoryAdapter (private val context: Context, private val mListData: List<CategoryModel>):
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = mListData[position]
        holder.tvCategory.text = category.tencd
        Picasso.get().load("${ApiService.imgCategory}${category.hinhcd}").into(holder.imgCategory)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductByCategoryActivity::class.java)
            intent.putExtra("catID", category.macd)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imgCategory: ImageView = itemView.findViewById(R.id.imgCategory)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
    }
}