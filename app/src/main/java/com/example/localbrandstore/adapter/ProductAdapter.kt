package com.example.localbrandstore.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.localbrandstore.R
import com.example.localbrandstore.activity.DetailProductActivity
import com.example.localbrandstore.model.ProductsModel
import com.example.localbrandstore.service.ApiService
import com.squareup.picasso.Picasso

    class ProductAdapter (private val context: Context,private var products: List<ProductsModel>):
        RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() , Filterable{

        private val dataOrigin: ArrayList<ProductsModel> = ArrayList(products)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
            return ProductViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val product = products[position]
            holder.tvProductName.text = product.tensp
            holder.tvProductPrice.text = product.giasp.toString()
            Picasso.get().load(ApiService.imgProductPath + product.hinhsp).into(holder.imgProduct)
            holder.itemView.setOnClickListener{
                val intent = Intent(context, DetailProductActivity::class.java)
                val bundleDetailProduct = Bundle()
                bundleDetailProduct.putString("maSanPham", product.masp)
                bundleDetailProduct.putString("name", product.tensp)
                bundleDetailProduct.putDouble("gia", product.giasp)
                bundleDetailProduct.putString("hinh", product.hinhsp)
                bundleDetailProduct.putString("mota", product.mota)
                intent.putExtras(bundleDetailProduct)
                context.startActivity(intent)

            }
        }

        override fun getItemCount(): Int {
            return products.size
        }

        override fun getFilter(): Filter {
            return ItemFilter()
        }

        inner class ItemFilter : Filter() {

            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val chuoitim = charSequence.toString().toLowerCase().trim()
                val filterResults = FilterResults()
                if (!TextUtils.isEmpty(chuoitim)) {
                    val tam = ArrayList<ProductsModel>()
                    for (sp in dataOrigin) {
                        if (sp.tensp.toLowerCase().contains(chuoitim)) {
                            tam.add(sp)
                        }
                    }
                    filterResults.values = tam
                    filterResults.count = tam.size
                } else {
                    filterResults.values = dataOrigin
                    filterResults.count = dataOrigin.size
                }
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    products = filterResults.values as ArrayList<ProductsModel>
                    notifyDataSetChanged()
                }
            }
        }


        class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
            val tvProductName: TextView = itemView.findViewById(R.id.tvNameProduct)
            val tvProductPrice: TextView = itemView.findViewById(R.id.tvPirceProduct)
        }


    }