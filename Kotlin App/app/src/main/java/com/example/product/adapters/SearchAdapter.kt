
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
import com.example.product.R
import com.example.product.Search.SearchModelClass
import java.lang.Exception

class SearchAdapter(var sList: List<SearchModelClass>, var context: Context?) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_file, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //  holder.imageView!!.setImageResource(sList[position].image)
        //   holder.textView.text = sList[position].text
        holder.recyclercard.setOnClickListener {
            val intent : Intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("title", sList.get(position).title)
            intent.putExtra("price" , sList.get(position).price)
            intent.putExtra("selling_price" , sList.get(position).selling_price)
            intent.putExtra("description", sList.get(position).description)
            intent.putExtra("postimages",sList.get(position).post_images)

            //flag for NOT displaying  edit,share and delete menu option for clicked from mypost
            intent.putExtra("flag",1)
            context!!.startActivity(intent)
        }
        holder.titleTextView.text = sList.get(position).title

        holder.priceTextView.text = sList.get(position).price.toString()

        if(sList.get(position).post_images == null)
        {
            holder.homeImageView.setImageResource(R.drawable.ic_launcher_foreground)
        }
        else{

            try {

                Glide.with(context!!)
                        .load(sList.get(position).post_images[0])
                        .into(holder.homeImageView)
            }
            catch (e: Exception){
                Log.d("d","$e")

            }
        }

    }

    override fun getItemCount(): Int {
        return sList.size
    }
/*
    override fun getFilter(): Filter {
        return exampleFilter
    }

    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<SearchModelClass> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(newList)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (item in newList) {
                    if (item.getText().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(charSequence: CharSequence, results: FilterResults) {
            sList.clear()
            sList.addAll(results.values as List<*>)
            notifyDataSetChanged()
        }
    }

    */

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var priceTextView : TextView
        var homeImageView: ImageView
        var recyclercard : CardView

        init {
            titleTextView = itemView.findViewById(R.id.titletextview)
            priceTextView  = itemView.findViewById(R.id.pricetextview)
            homeImageView =itemView.findViewById(R.id.homeimage)
            recyclercard = itemView.findViewById(R.id.recycler_card)


        }
        // var imageView: ImageView

    }
}


