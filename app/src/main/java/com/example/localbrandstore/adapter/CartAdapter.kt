package com.example.localbrandstore.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.localbrandstore.R
import com.example.localbrandstore.activity.CartActivity
import com.example.localbrandstore.model.Products
import com.example.localbrandstore.service.ApiService
import com.example.localbrandstore.sqlite.ProductDAO
import com.squareup.picasso.Picasso

class CartAdapter(private val context: Context, private val mListData: ArrayList<Products>):
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val productsDAO: ProductDAO = ProductDAO(context.applicationContext)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val sp = mListData[position]
        holder.productName.text = sp.tensp
        holder.productPrice.text = sp.giasp.toString()
        holder.cartQuality.text = sp.soLuong.toString()
        Picasso.get().load(ApiService.imgProductPath + sp.hinhsp).into(holder.productImg)

        holder.cartDelete.setOnClickListener {
            notifications(sp)
        }

        holder.numberMax.setOnClickListener {
            sp.soLuong += 1
            holder.cartQuality.text = sp.soLuong.toString()
            updateTotal()
            // cập nhật số lượng
            productsDAO.updateProductQuantityById(sp.masp, sp.soLuong)
        }

        holder.numberMin.setOnClickListener {
            if (sp.soLuong > 1) {
                sp.soLuong -= 1
                holder.cartQuality.text = sp.soLuong.toString()
                updateTotal()
                // cập nhật số lượng
                productsDAO.updateProductQuantityById(sp.masp, sp.soLuong)
            }
        }

        updateTotal()
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    private fun notifications(sp: Products) {
        val builder1 = AlertDialog.Builder(context)
        builder1.setMessage("Bạn có chắc muốn xóa sản phẩm này ?")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "No"
        ) { dialog, _ -> dialog.cancel() }

        builder1.setNegativeButton(
            "Yes"
        ) { dialog, _ ->
            // get the position of the item to be removed
            productsDAO.deleteProductById(sp.masp)
            mListData.remove(sp)
            notifyDataSetChanged()
            updateTotal()

            Toast.makeText(context, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        val alert11 = builder1.create()
        alert11.show()
    }

    private fun updateTotal() {
        var total = 0.0
        for (sp in mListData) {
            total += sp.giasp * sp.soLuong
        }
        (context as CartActivity).updateTotal(total)
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ánh xạ
        var productImg: ImageView = itemView.findViewById(R.id.cart_img)
        var productName: TextView = itemView.findViewById(R.id.cart_name)
        var productPrice: TextView = itemView.findViewById(R.id.cart_price)
        var numberMin: TextView = itemView.findViewById(R.id.cart_min)
        var cartQuality: TextView = itemView.findViewById(R.id.cart_quantity)
        var numberMax: TextView = itemView.findViewById(R.id.cart_max)
        var cartDelete: Button = itemView.findViewById(R.id.cart_delete)
    }
}