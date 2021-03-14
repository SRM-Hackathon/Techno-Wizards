package com.example.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.product.Models.Model_Home
import com.example.product.adapters.MyPostAdapter
import com.example.product.adapters.ProductAdapter


class Products : Fragment() {

    val myproductlist=ArrayList<Model_Home>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v=inflater.inflate(R.layout.fragment_products, container, false)
        var recyclerView = v.findViewById(R.id.myproductrecyclerviewid) as RecyclerView

        val adapter = ProductAdapter(myproductlist, context)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        return v
    }
}