package com.example.localbrandstore.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.localbrandstore.MainActivity
import com.example.localbrandstore.R
import com.example.localbrandstore.activity.DetailProductActivity
import com.example.localbrandstore.model.ProductsModel
import com.example.localbrandstore.service.ApiService
import com.squareup.picasso.Picasso

class ProductByCategoryAdapter (private val context: Context, private var mListData: List<ProductsModel>):
    RecyclerView.Adapter<ProductByCategoryAdapter.ProductByCategoryViewHolder>(), Filterable {

    private var dataOrigin: ArrayList<ProductsModel> = ArrayList(mListData)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductByCategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product_by_category, parent, false)
        return ProductByCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductByCategoryViewHolder, position: Int) {
        val products = mListData[position]
        holder.tvNameProduct.text = products.tensp
        holder.tvPriceProduct.text = products.giasp.toString()
        Picasso.get().load(ApiService.imgProductPath + products.hinhsp).into(holder.imgProduct)
        holder.itemView.setOnClickListener{
            val intent = Intent(context, MainActivity::class.java)
            val bundleDetailProduct = Bundle()
            bundleDetailProduct.putString("maSanPham", products.masp)
            bundleDetailProduct.putString("name", products.tensp)
            bundleDetailProduct.putDouble("gia", products.giasp)
            bundleDetailProduct.putString("hinh", products.hinhsp)
            bundleDetailProduct.putString("mota", products.mota)
            intent.putExtras(bundleDetailProduct)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun getFilter(): Filter {
        return ItemFilter()
    }

    private inner class ItemFilter: Filter(){
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val chuoitim = charSequence.toString().toLowerCase().trim()
            val filterResults = FilterResults()
            if (!chuoitim.isEmpty()) {
                val tam = ArrayList<ProductsModel>()
                for (sp in dataOrigin) {
                    if (sp.tensp.toLowerCase().contains(chuoitim))
                        tam.add(sp)
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
            if (filterResults.count > 0) {
                mListData = filterResults.values as ArrayList<ProductsModel>
                notifyDataSetChanged()
            }
        }
    }

    class ProductByCategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
        var tvNameProduct: TextView = itemView.findViewById(R.id.tvNameProduct)
        var tvPriceProduct: TextView = itemView.findViewById(R.id.tvPriceProduct)
    }


}