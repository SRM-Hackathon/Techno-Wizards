package com.example.product.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.product.Models.Model_Category
import com.example.product.R
import com.example.product.adapters.CategoryAdapter.MyViewHolder1

class CategoryAdapter(var mList1: ArrayList<Model_Category>, var context: Context?) : RecyclerView.Adapter<MyViewHolder1>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder1 {
        val layoutInflater1 = LayoutInflater.from(context)
        val view1 = layoutInflater1.inflate(R.layout.layout_file1, parent, false)
      //  val v1 = layoutInflater1.inflate(R.layout.layout_file1, parent, false)
        return MyViewHolder1(view1)
    }

    override fun onBindViewHolder(holder1: MyViewHolder1, position: Int) {

        holder1.textView1.text=mList1[position].text1
        holder1.imageView1.setImageResource(mList1[position].image1)

    }

    override fun getItemCount(): Int {
        return mList1.size
    }

    inner class MyViewHolder1(itemView1: View) : RecyclerView.ViewHolder(itemView1) {
        var imageView1: ImageView
        var textView1: TextView

        init {
            imageView1 = itemView1.findViewById(R.id.layoutimageViewId1)
            textView1 = itemView1.findViewById(R.id.layouttextViewId1)
        }
    }

}