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

class MyPostAdapter(var MyPostItems: MutableList<Model_Home>,
                    var context: Context?) : RecyclerView.Adapter<MyPostAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.layout_mypost_recyclercard, parent, false)

        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


       holder.constraintclick.setOnClickListener{
           performIntent(position)
       }


       holder.cardView.setOnClickListener {
           performIntent(position)
       }

        holder.titleTextView.text =  MyPostItems.get(position).title
        holder.priceTextView.text = MyPostItems.get(position).price.toString()
        holder.sellingPriceTextView.text =  MyPostItems.get(position).selling_price.toString()


        if(MyPostItems.get(position).post_images == null)
        {
            holder.profileprdtimgView.setImageResource(R.drawable.ic_launcher_foreground)
        }
        else{

            try {

                Glide.with(context!!)
                        .load(MyPostItems.get(position).post_images[0])
                        .into(holder.profileprdtimgView)
            }
            catch (e: Exception){
                Log.d("d","$e")

            }
        }

        //Log.d("IMGAA", "I -: ${MyPostItems.get(position).post_images[0]}")



    }

    fun performIntent(position : Int){
        val intent : Intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("title", MyPostItems.get(position).title)
        intent.putExtra("_id", MyPostItems.get(position).id)
        intent.putExtra("postid",MyPostItems.get(position).postid)
        intent.putExtra("price" , MyPostItems.get(position).price)
        intent.putExtra("selling_price" , MyPostItems.get(position).selling_price)
        intent.putExtra("description", MyPostItems.get(position).description)
        intent.putExtra("postimages",MyPostItems.get(position).post_images)
//        intent.putExtra("lat",MyPostItems.get(position).location)
//        intent.putExtra("long",MyPostItems.get(position).long)
        intent.putExtra("city",MyPostItems.get(position).city)

      //  intent.putExtra("pincode",MyPostItems.get(position).)
        intent.putExtra("flag",2) //flag for displaying  edit,share and delete menu option for clicked from mypost
        intent.putExtra("dflag",1)  // flag for deletion notify
        context!!.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return  MyPostItems.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.titletextview)
//        var desTextView : TextView = itemView.findViewById(R.id.destextview)
        var priceTextView : TextView = itemView.findViewById(R.id.pricetextview)
        var sellingPriceTextView : TextView = itemView.findViewById(R.id.sellingpricetextview)
        var constraintclick:ConstraintLayout=itemView.findViewById(R.id.constraint)
        var cardView : CardView = itemView.findViewById(R.id.cardView)
        var profileprdtimgView:ImageView=itemView.findViewById(R.id.profileprdtimg)
    }

}
