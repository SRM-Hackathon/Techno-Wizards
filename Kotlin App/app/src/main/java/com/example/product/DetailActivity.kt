package com.example.product

import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.example.product.Api.ApiService
import com.example.product.Api.RetrofitInstance
import com.example.product.adapters.ViewPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailActivity : AppCompatActivity(), OnDateSetListener {

    private var actionBar: ActionBar? = null
    private var ViewPager: ViewPager? = null
    var sliderDotspanel: LinearLayout? = null
    var pricePerDay : Int? = null

    val SHARED_PREF_NAME="pref"
    val KEY_JWT="jwt"

    var bitlist=ArrayList<Bitmap>()

  private  val imageList=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        var viewPager = findViewById<ViewPager>(R.id.viewPager)
        val adapter = ViewPagerAdapter(this,imageList)


        var sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE)
        var jwttokencheck=sharedPreferences.getString(KEY_JWT,null)


        //INITIALIZING VIEWS
        val optionbtn = findViewById<ImageView>(R.id.option_button) as ImageView
        val priceTextView = findViewById<TextView>(R.id.priceamount) as TextView
        val perDayButton : Button? = findViewById<Button>(R.id.per_day) as Button
        val perWeekButton : Button? = findViewById<Button>(R.id.per_week) as Button
        val perMonthButton : Button? = findViewById<Button>(R.id.per_month) as Button
        val titleTextView : TextView = findViewById<TextView>(R.id.dtitle) as TextView
        val aboutTextView : TextView = findViewById<TextView>(R.id.about) as TextView
        val priceAmountTextView : TextView = findViewById<TextView>(R.id.priceamount) as TextView
        val sellingPriceTextView : TextView = findViewById<TextView>(R.id.sellingpriceamount) as TextView


        //INITIALIZING VALUES
        val titleValue = intent.getStringExtra("title")
        val idValue = intent.getStringExtra("_id")
        val postidValue=intent.getStringExtra("postid")
        val priceValue = intent.getIntExtra("price", 0)
        val sellingPriceValue = intent.getIntExtra("selling_price", 0)
        val aboutTextValue = intent.getStringExtra("description")
        val numbersList= intent.getSerializableExtra("postimages") as ArrayList<String>?
//        val latValue=intent.getStringExtra("lat")
//        val longValue=intent.getStringExtra("long")
        val cityValue=intent.getStringExtra("city")
     //   Log.d("server","${numbersList},${latValue},$longValue")
        var dflag=intent.getIntExtra("dflag",0)

        println("duck ${numbersList}")

//        for (i in 0..numbersList!!.size-1){
//            Picasso.get()
//                    .load(numbersList!![i])
//                    .into(object: Target {
//                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                        //    println("navel $fileUris")
//                            println("itch $bitlist")
//
//                        }
//
//                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//
//                        }
//
//                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?){
//
//                            //      fileUris.add(getImageUriArray(this@EditActivity,bitmap!!))
//                            //  var shit=getImageUriArray(bitmap!!)
//                            //  println("chod ${getImageUriArray(this@EditActivity,bitmap!!)}")
//                            // getImageUriArray(this@EditActivity,bitmap!!)
//                           // fileUris.add(getImageUri(this@EditActivity,bitmap!!))
//                      //      bitlist.add(bitmap!!)
//                            println("itch $bitlist")
//
//
//                        }
//                    })
//
//        }



        //NULL CHECK
        if(titleValue != null || idValue != null || priceValue != null || sellingPriceTextView != null || aboutTextValue != null|| numbersList!=null){
            titleTextView.text = titleValue
            priceAmountTextView.text = priceValue.toString()
            pricePerDay = priceValue
            sellingPriceTextView.text = sellingPriceValue.toString()
            aboutTextView.text = aboutTextValue
           Log.d("server","${numbersList}")
            if(numbersList!=null){
                Log.d("server","${numbersList}")


                for(i in numbersList){
                      imageList.add(i)

                  }}

        }
        viewPager.adapter=adapter
        viewPager.setPadding(130, 0, 130, 0)


        var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
                object : ViewPager.OnPageChangeListener{
                    override fun onPageScrollStateChanged(state: Int) {
                        // your logic here
                    }
                    override fun onPageScrolled(position: Int,positionOffset: Float,
                                                positionOffsetPixels: Int) {
                        // your logic here
                    }
                    override fun onPageSelected(position: Int) {
                        // your logic here
                    }
                }


        val flag=intent.getIntExtra("flag",0)!!
        if(flag==1){
            optionbtn.visibility=View.INVISIBLE
            optionbtn.isEnabled=false
        }

        else if (flag==2){
            //EDITPAGE CODE
            optionbtn.visibility=View.VISIBLE
            optionbtn.isEnabled=true
            optionbtn.setOnClickListener {
                val popupMenu=PopupMenu(this,it)
                popupMenu.inflate(R.menu.edit_delete_share_menu)
                popupMenu.show()

                popupMenu.setOnMenuItemClickListener {item->
                    when(item.itemId){
                        R.id.editid ->{
                            val i : Intent = Intent(this,EditActivity::class.java)
                            i.putExtra("_id",idValue)
                            i.putExtra("postid",postidValue)
                            i.putExtra("etitle",titleValue)
                            i.putExtra("erentprice",priceValue)
                            i.putExtra("esellingprice",sellingPriceValue)
                            i.putExtra("edescription",aboutTextValue)
                            i.putExtra("postimages",numbersList)
//                            i.putExtra("lat",latValue)
//                            i.putExtra("long",longValue)
                            i.putExtra("city",cityValue)
                            this!!.startActivity(i)
                       //     Toast.makeText(this,"edit",Toast.LENGTH_SHORT).show()
                            true
                        }
                        R.id.deleteid ->{
                            if (idValue != null) {
                                deleteFunction(idValue, jwttokencheck!!)
                                val intent:Intent=Intent(applicationContext,MainActivity::class.java)
                                intent.putExtra("dflag",1)
                                startActivity(intent)
                            }
                            true
                        }
                        R.id.shareid ->{
                            Toast.makeText(this,"share",Toast.LENGTH_SHORT).show()
                            true
                        }

                        else -> false
                    }
                }
            }
        }

        actionBar = supportActionBar
        ViewPager = findViewById(R.id.image)
        sliderDotspanel = findViewById<View>(R.id.Slidedots) as LinearLayout

        //CALENDAR
        val calender = findViewById<ImageView>(R.id.calender)
        calender.setOnClickListener {
            val datapicker: DialogFragment = dataPickerFragment()
            datapicker.show(supportFragmentManager, "date picker")
        }


        //PRICE CALCULATION
        perWeekButton!!.setOnClickListener(View.OnClickListener {

            if(pricePerDay !=  null){
                val totalAmount = calculatePrice(pricePerDay!!,7)
                priceTextView.text = totalAmount.toString()
            }
        })

        perDayButton!!.setOnClickListener(View.OnClickListener {
          priceTextView!!.text = pricePerDay.toString()
        });

        perMonthButton!!.setOnClickListener(View.OnClickListener {

            if(pricePerDay != null){
                val totalAmount = calculatePrice(pricePerDay!! , 30)
                priceTextView.text = totalAmount.toString()
            }

        })

//        val refresh = Intent(this, DetailActivity::class.java)
//        startActivity(refresh) //Start the same Activity
//
//        finish()
    }

    private fun calculatePrice(price : Int, days : Int) : Int{
        return price * days
    }


    private fun deleteFunction(ident : String, jwt : String) {
        Log.d("MyTag", ident)
        RetrofitInstance.BASE_URL="http://rentapp.ddns.net:3000"
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        val items = HashMap<String, String>()
        items["_id"] = ident
        Log.d("Token", "This is Delete Function ${jwt}")
        retIn.deleteMyPost(jwt, items).enqueue(object : Callback<com.example.product.Models.Response>{
            override fun onResponse(call: Call<com.example.product.Models.Response>, response: Response<com.example.product.Models.Response>) {
                Toast.makeText(applicationContext, "Item Delete", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<com.example.product.Models.Response>, t: Throwable) {
                Toast.makeText(applicationContext, "Item failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c[Calendar.YEAR] = year
        c[Calendar.MONTH] = month
        c[Calendar.DAY_OF_MONTH] = dayOfMonth
        val CurrentDatestring = DateFormat.getDateInstance(DateFormat.FULL).format(c.time)
        val date = findViewById<TextView>(R.id.date)
        date.text = CurrentDatestring
    }



}