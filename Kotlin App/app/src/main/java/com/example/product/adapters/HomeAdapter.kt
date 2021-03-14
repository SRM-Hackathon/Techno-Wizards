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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.product.DetailActivity
import com.example.product.Models.Model_Home
import com.example.product.R
import java.lang.Exception

class HomeAdapter(var mList: List<Model_Home>,
                  var context: Context?) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.layout_file, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.recyclercard.setOnClickListener {
            val intent : Intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("title", mList.get(position).title)
            intent.putExtra("price" , mList.get(position).price)
            intent.putExtra("selling_price" , mList.get(position).selling_price)
            intent.putExtra("description", mList.get(position).description)
            intent.putExtra("postimages",mList.get(position).post_images)

            //flag for NOT displaying  edit,share and delete menu option for clicked from mypost
            intent.putExtra("flag",1)
            context!!.startActivity(intent)
        }


        holder.titleTextView.text = mList.get(position).title
        holder.priceTextView.text = mList.get(position).price.toString()

        if(mList.get(position).post_images == null)
        {
            holder.homeImageView.setImageResource(R.drawable.ic_launcher_foreground)
        }
        else{

            try {

                Glide.with(context!!)
                        .load(mList.get(position).post_images[0])
                        .into(holder.homeImageView)
            }
            catch (e: Exception){
                Log.d("d","$e")

            }
        }

    //    Log.d("IMGAA", "I -: ${mList.get(position).post_images[0]}")




    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.titletextview)
        var recyclercard : CardView = itemView.findViewById(R.id.recycler_card)
        var priceTextView : TextView = itemView.findViewById(R.id.pricetextview)
        var homeImageView: ImageView =itemView.findViewById(R.id.homeimage)
  }

    }
