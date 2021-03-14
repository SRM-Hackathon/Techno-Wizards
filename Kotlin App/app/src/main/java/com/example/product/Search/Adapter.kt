package com.example.product.Search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.product.R

class Adapter(private val searchAsYouTypeList: List<SearchAsYouTypeModel>, private val context: Context) : RecyclerView.Adapter<Adapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = searchAsYouTypeList[position].title
        holder.email.text = searchAsYouTypeList[position].title1
        holder.itemcard.setOnClickListener(View.OnClickListener { v ->
            val i = Intent(v.context, SearchActivity::class.java)
            i.putExtra("query",searchAsYouTypeList[position].title.toString())
            v.context.startActivity(i)
        }
        )
    }

    override fun getItemCount(): Int {
        return searchAsYouTypeList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var email: TextView
        var itemcard:CardView

        init {
            name = itemView.findViewById(R.id.t1)
            email = itemView.findViewById(R.id.t2)
            itemcard=itemView.findViewById(R.id.itemcard)



        }
    }

}