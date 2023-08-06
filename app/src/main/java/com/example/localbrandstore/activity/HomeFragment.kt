package com.example.localbrandstore.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.localbrandstore.R
import com.example.localbrandstore.adapter.CategoryAdapter
import com.example.localbrandstore.adapter.ProductAdapter
import com.example.localbrandstore.adapter.SlideAdapter
import com.example.localbrandstore.model.CategoryModel
import com.example.localbrandstore.model.ProductsModel
import com.example.localbrandstore.model.SlideModel
import com.example.localbrandstore.service.ApiService
import com.example.localbrandstore.service.RetrofitClient
import com.example.localbrandstore.util.AndroidToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private var dataSlide: List<SlideModel> = emptyList()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var slideAdapter: SlideAdapter

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var rvCategory: RecyclerView

    private lateinit var edtSearchProduct: EditText

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val apiService = RetrofitClient.getApiService()
    private val androidToast: AndroidToast = AndroidToast()

    private lateinit var btnCart: ImageView

    val runnable: Runnable = Runnable {
        if (viewPager2.currentItem == dataSlide.size - 1) {
            viewPager2.currentItem = 0
        } else {
            viewPager2.currentItem = viewPager2.currentItem + 1
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_home, container, false)

        initView(view)
        eventClick()
        getListProduct()
        getListSlides()
        getListCategory()
        searchProduct()

        return view
    }

    private fun getListSlides() {
        val dataSlides = ArrayList<SlideModel>()
        dataSlides.add(SlideModel("${ApiService.imgSlidePath}slide01.jpeg", "link"))
        dataSlides.add(SlideModel("${ApiService.imgSlidePath}slide02.jpeg", "link"))
        dataSlides.add(SlideModel("${ApiService.imgSlidePath}slide03.jpeg", "link"))

        slideAdapter = SlideAdapter(dataSlides, requireContext())
        viewPager2.adapter = slideAdapter

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 2000)
            }
        })
    }

    private fun eventClick(){
        btnCart.setOnClickListener{
            var intent = Intent(context, CartActivity::class.java)
            startActivity(intent)
        }
    }


    private fun getListCategory(){
        apiService.getListCategory().enqueue(object : Callback<List<CategoryModel>>{
            override fun onResponse(
                call: Call<List<CategoryModel>>,
                response: Response<List<CategoryModel>>
            ) {
                if (response.isSuccessful){
                    val category = response.body()
                    if (category != null){
                        categoryAdapter = CategoryAdapter(requireContext(), category)
                        productAdapter.notifyDataSetChanged()
                        rvCategory.adapter = categoryAdapter
                    }else{
                        androidToast.showToast(requireContext(), "Lỗi")
                    }
                }
            }

            override fun onFailure(call: Call<List<CategoryModel>>, t: Throwable) {
                androidToast.showToast(requireContext(), "Call API Err")
            }
        })
    }


    private fun getListProduct(){
        apiService.getListProducts().enqueue(object : Callback<List<ProductsModel>>{
            override fun onResponse(
                call: Call<List<ProductsModel>>,
                response: Response<List<ProductsModel>>
            ) {
                if (response.isSuccessful) {
                    val products = response.body()
                    if (products != null) {
                        productAdapter = ProductAdapter(requireContext(),products)
                        productAdapter.notifyDataSetChanged()
                        recyclerView.adapter = productAdapter
                    }
                } else {
                    androidToast.showToast(requireContext(),"Lỗi")
                }
            }

            override fun onFailure(call: Call<List<ProductsModel>>, t: Throwable) {
                androidToast.showToast(requireContext(),"Call API Err")
            }

        })
    }

    private fun searchProduct(){
        edtSearchProduct.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val chuoitim = charSequence.toString()
                productAdapter.filter.filter(chuoitim)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }


    private fun initView(view: View){
        viewPager2 = view.findViewById(R.id.home_viewPager)
        rvCategory = view.findViewById(R.id.home_category)
        recyclerView = view.findViewById(R.id.rvProducts)
        edtSearchProduct = view.findViewById(R.id.home_edtSeach)
        btnCart = view.findViewById(R.id.btnCart)

        rvCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(requireContext(), emptyList())
        rvCategory.adapter = categoryAdapter

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        productAdapter = ProductAdapter(requireContext(),emptyList())
        recyclerView.adapter = productAdapter
    }

}