package com.example.product.Home

import android.app.Activity
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.product.Api.ApiService
import com.example.product.Api.RetrofitInstance
import com.example.product.GPSTracker
import com.example.product.GpsUtils
import com.example.product.Models.IpReturnType
import com.example.product.Models.Model_Category
import com.example.product.Models.Model_Home
import com.example.product.R
import com.example.product.Search.SearchtypeActivity
import com.example.product.adapters.CategoryAdapter
import com.example.product.adapters.HomeAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Home : Fragment() {

    val mList=ArrayList<Model_Home>()
    val mList1=ArrayList<Model_Category>()
    var latitude: String? = null
    var longitude: String? = null
    val SHARED_PREF_NAME="pref"
    val KEY_JWT="jwt"
    val KEY_USER_ID="userid"
    var MY_PERMISSIONS_REQUEST = 100



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_home, container, false)

        var sharedPreferences = activity?.getSharedPreferences(SHARED_PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        var jwttokencheck = sharedPreferences?.getString(KEY_JWT, null)
        var useridcheck=sharedPreferences?.getString(KEY_USER_ID,null)


        var recyclerView = v.findViewById(R.id.recyclerviewid) as RecyclerView
        //var recyclerView1 = v.findViewById(R.id.recyclerviewid1) as RecyclerView

        val gpsTracker = GPSTracker(context)
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            val stringLatitude = gpsTracker.latitude.toString()
            println("shit $stringLatitude")

            val stringLongitude = gpsTracker.longitude.toString()
            println("shityt $stringLongitude")

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert()
        }
        //Get phone latitude and longitude.

        GpsUtils.getInstance().findDeviceLocation(context as Activity?)
        latitude = GpsUtils.getInstance().latitude
        longitude = GpsUtils.getInstance().longitude
        Log.d("Act", "This is Latitude and Long $latitude $longitude")
        //IP
        val wifiMan= context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInf: WifiInfo = wifiMan.getConnectionInfo()
        val ipAddress = wifiInf.ipAddress
        val ipaddress = String.format("%d.%d.%d.%d", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff)
        //MAC
        val wifiMgr = context?.getSystemService(WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiMgr.connectionInfo
        var mac=wifiInfo.macAddress



      println("bitchip $ipaddress, mac $mac")


        if (latitude == null || longitude == null) {
            //ip
            val wifiMan= context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInf: WifiInfo = wifiMan.getConnectionInfo()
            val ipAddress = wifiInf.ipAddress
            val ipaddress = String.format("%d.%d.%d.%d", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff)

            CoordinateFinding(ipaddress)


        } else {

              fetchHomeDetails(latitude!!.toDouble(), longitude!!.toDouble())
            //fetchallpost()


        }
            val adapter1 = CategoryAdapter(mList1, context)
            mList1.add(Model_Category(R.drawable.ic_launcher_foreground, "ctg1"))
            mList1.add(Model_Category(R.drawable.ic_launcher_foreground, "ctg1"))
            mList1.add(Model_Category(R.drawable.ic_launcher_foreground, "ctg1"))

//            recyclerView1.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
//            recyclerView1.adapter = adapter1

            var btnsearch = v.findViewById<View>(R.id.search_btn) as Button
            btnsearch.setOnClickListener {
                val `in` = Intent(activity, SearchtypeActivity::class.java)
                `in`.putExtra("some", "somedata")
                startActivity(`in`)
            }


        return v
    }

    private fun CoordinateFinding(ipadress:String){
        println("passed ip $ipadress")
        RetrofitInstance.BASE_URL = "http://ip-api.com/"
        var retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        retIn.getCoordinate(ipadress).enqueue(object :Callback<IpReturnType>{
            override fun onFailure(call: Call<IpReturnType>, t: Throwable) {
                Toast.makeText(context,"seruppado",Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<IpReturnType>, response: Response<IpReturnType>) {
                var rlat=response.body()!!.lat
                var rlon=response.body()!!.lon
                println("coordinate $rlat $rlon")
                fetchHomeDetails(rlat.toDouble(), rlon.toDouble())
            }

        })
    }
   private fun fetchHomeDetails(lat:Double, lon: Double){
       val SHARED_PREF_NAME="pref"
       val KEY_JWT="jwt"
       val KEY_USER_ID="userid"

       var sharedPreferences = activity?.getSharedPreferences(SHARED_PREF_NAME, AppCompatActivity.MODE_PRIVATE)
       var jwttokencheck = sharedPreferences?.getString(KEY_JWT, null)
       var useridcheck=sharedPreferences?.getString(KEY_USER_ID,null)

       println("useridcheck $useridcheck")

       RetrofitInstance.BASE_URL = "http://rentapp.ddns.net:3000"
       val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
       retIn.fetchHomePost(jwttokencheck!!,lat,lon).enqueue(object :Callback<List<Model_Home>>{
           override fun onFailure(call: Call<List<Model_Home>>, t: Throwable) {

           }

           override fun onResponse(call: Call<List<Model_Home>>, response: Response<List<Model_Home>>) {
               Log.d("surash","${response.body()!!}")
               var recyclerView = activity?.findViewById(R.id.recyclerviewid) as? RecyclerView
               val adapter = HomeAdapter(mList, context)
               recyclerView!!.layoutManager = GridLayoutManager(activity, 2)
               recyclerView!!.adapter = adapter
               Log.d("Response", response.toString())
               if (response == null) {
                   Toast.makeText(context, "No Post", Toast.LENGTH_SHORT).show()
                   return
               }
               else {
                   Log.d("Response", "${response.body()}")

                   var response = response.body()!!

                   for (i in response) {
                       println("useridcheck $$useridcheck,${i.id}")
                       if(useridcheck!=i.id) {
                           mList.add(Model_Home(i.id, i.title, i.description, i.price, i.selling_price, i.city, i.post_images, i.postid))
                       }

                   }

                    println("yamaha ${mList}")
               }

           }
       })

   }

}









