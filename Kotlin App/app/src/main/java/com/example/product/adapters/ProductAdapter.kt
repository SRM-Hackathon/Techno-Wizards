package com.example.product.adapters
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.product.DetailActivity
import com.example.product.Models.Model_Home
import com.example.product.R
import java.lang.Exception

class ProductAdapter(var myproductList: MutableList<Model_Home>,
                     var context: Context?):RecyclerView.Adapter<ProductAdapter.MyViewHolder3>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder3 {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.layout_mypost_recyclercard, parent, false)

        return MyViewHolder3(view)

    }

    override fun getItemCount(): Int {
        return  myproductList.size

    }

    override fun onBindViewHolder(holder: MyViewHolder3, position: Int) {
        holder.titleTextView.text =  myproductList.get(position).title
        holder.priceTextView.text = myproductList.get(position).price.toString()
        holder.sellingPriceTextView.text =  myproductList.get(position).selling_price.toString()


    }


inner class MyViewHolder3(itemView1: View) : RecyclerView.ViewHolder(itemView1) {
    var imageView1: ImageView
    var textView1: TextView

    init {
        imageView1 = itemView1.findViewById(R.id.layoutimageViewId1)
        textView1 = itemView1.findViewById(R.id.layouttextViewId1)
    }
    var titleTextView: TextView = itemView.findViewById(R.id.titletextview)
    var priceTextView : TextView = itemView.findViewById(R.id.pricetextview)
    var sellingPriceTextView : TextView = itemView.findViewById(R.id.sellingpricetextview)


}


}