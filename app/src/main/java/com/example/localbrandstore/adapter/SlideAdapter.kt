package com.example.localbrandstore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.localbrandstore.R
import com.example.localbrandstore.model.SlideModel
import com.squareup.picasso.Picasso

class SlideAdapter (private val imgSlide: List<SlideModel>, private val context: Context):
    RecyclerView.Adapter<SlideAdapter.SlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slide, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        val image = imgSlide[position]
        Picasso.get().load(image.url).into(holder.imgSlide)
    }

    override fun getItemCount(): Int {
        return imgSlide.size
    }

    inner class SlideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSlide: ImageView = itemView.findViewById(R.id.imgSlide)
    }
}