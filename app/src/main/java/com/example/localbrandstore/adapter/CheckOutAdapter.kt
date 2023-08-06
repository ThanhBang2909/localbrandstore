package com.example.localbrandstore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.localbrandstore.R
import com.example.localbrandstore.model.Products
import com.example.localbrandstore.model.ProductsModel
import com.example.localbrandstore.service.ApiService
import com.example.localbrandstore.sqlite.ProductDAO
import com.squareup.picasso.Picasso

class CheckOutAdapter (private val context: Context, private val mListProduct: ArrayList<Products>):
    RecyclerView.Adapter<CheckOutAdapter.CheckoutViewHolder>() {

    private val productsDAO: ProductDAO = ProductDAO(context.applicationContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false)
        return CheckoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        val sp = mListProduct[position]
        holder.cartName.text = sp.tensp
        holder.cartPrice.text = sp.giasp.toString()
        holder.cartQuality.text = sp.soLuong.toString()
        Picasso.get().load(ApiService.imgProductPath + sp.hinhsp).into(holder.cartImg)
    }

    override fun getItemCount(): Int {
        return mListProduct.size
    }

    inner class CheckoutViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val cartImg: ImageView = itemView.findViewById(R.id.cart_img)
        val cartName: TextView = itemView.findViewById(R.id.cart_name)
        val cartPrice: TextView = itemView.findViewById(R.id.cart_price)
        val cartQuality: TextView = itemView.findViewById(R.id.cart_quantity)
    }
}