package com.example.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.product.Api.ApiService
import com.example.product.Api.RetrofitInstance
import com.example.product.Models.Model_Home
import com.example.product.adapters.MyPostAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.SharedPreferences
import com.google.firebase.messaging.FirebaseMessaging


class Profile : Fragment() {

    val mypostList=ArrayList<Model_Home>()

    private val SHARED_PREF_NAME="pref"
    private val KEY_JWT="jwt"
    private val KEY_USER_ID="userid"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_profile, container, false)
        FirebaseMessaging.getInstance().subscribeToTopic("general")
        var recyclerView = v.findViewById(R.id.MyPost_recylerview) as RecyclerView
        RetrofitInstance.BASE_URL="http://rentapp.ddns.net:3000"
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)

        Log.d("mypost", "This  is my Post ${mypostList}")

        var sharedPreferences= activity?.getSharedPreferences(SHARED_PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        var jwttokencheck = sharedPreferences?.getString(KEY_JWT,null)
        var useridcheck=sharedPreferences?.getString(KEY_USER_ID,null)


        //FETCHES MY_POST DATA

        retIn.fetchMyPost(jwttokencheck!!).enqueue(object : Callback<List<Model_Home>>{
            override fun onResponse(call: Call<List<Model_Home>>, response: Response<List<Model_Home>>) {
                println("sureka ${response.body()!!}")

                val adapter = MyPostAdapter(mypostList, context)
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = adapter


                var response=response.body()!!

                if(useridcheck==null && response.isNotEmpty()){
                    var userideditor: SharedPreferences.Editor=sharedPreferences!!.edit()
                    var userid=response[0].id
                    userideditor.putString(KEY_USER_ID,userid)
                    userideditor.apply()
                }

                Log.d("Response", response.toString())

                for(i in response){

                    if (i == null) {
                        Log.d("Main", "Error")
                    } else {
                        mypostList.add(Model_Home(i.id,i.title, i.description, i.price, i.selling_price, i.city,i.post_images,i.postid))
                        Log.d("mypost", "This  is my inside Retrofit ${mypostList}")


                    }
                }

                adapter.notifyDataSetChanged()

            }




            override fun onFailure(call: Call<List<Model_Home>>, t: Throwable) {
                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
            }

        })

        return v

    }


    }

