package com.example.product.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.product.R
import java.lang.Exception
import java.util.*


class ViewPagerAdapter(private val mContext: Context, private val itemList: ArrayList<String>) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater!!.inflate(R.layout.viewpager_item_for_detailactivity, container, false)

        val image=view.findViewById<ImageView>(R.id.detail_list_image)
     //   val model=itemList[position]
     //   val img=model.image
        Log.d("glide","image ${itemList}")
        try {
            Glide.with(this@ViewPagerAdapter.mContext)
                    .load(itemList[position])
                    .into(image)
            container.addView(view, position)

        } catch (e: Exception) {
            Log.d("", "$e")

        }

        return view
    }


    override fun getCount(): Int {
        return itemList.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}