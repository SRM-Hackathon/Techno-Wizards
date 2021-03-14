package com.example.product.Search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.product.Api.ApiService
import com.example.product.Api.RetrofitInstance
import com.example.product.R
import com.example.product.adapters.SearchAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SearchActivity : AppCompatActivity() {


    val sList=ArrayList<SearchModelClass>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        var searchView = findViewById<SearchView>(R.id.searchView3)


        searchView.isIconifiedByDefault = false
        searchView.isFocusable = false
        searchView.isIconified = true
        //searchView.requestFocusFromTouch();
        //   searchView.clearFocus();          IF WE USE AUTO SUBMIT FOR SEARCHVIEW, THE searchView.clearFocus() LINE SHLD BE PRESENT.
        searchView = findViewById(R.id.searchView3)

        setSupportActionBar(toolbar)
        searchView.setOnClickListener(View.OnClickListener {
            val `in` = Intent(this@SearchActivity, SearchtypeActivity::class.java)
            startActivity(`in`)
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                //    searchAdapter.getFilter().filter(s);

                searchOnRetroFit(s)

                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })

        //THIS LINE SHOULD BE PRESENT AFTER THE setOnQueryTextListener() FOR ACHIEVING AUTO QUERY SUBMIT. OR ELSE IT WONT WORK
        if (intent.extras != null) {
            val title = intent.getStringExtra("query")
            println("bitch $title")
            searchView.setQuery(title, true)

        }

    }

    private fun searchOnRetroFit(query: String) {
        val SHARED_PREF_NAME="pref"
        val KEY_JWT="jwt"
        var sharedPreferences= getSharedPreferences(SHARED_PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        var jwttokencheck = sharedPreferences?.getString(KEY_JWT,null)

        RetrofitInstance.BASE_URL="http://rentapp.ddns.net:3000"
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)

        retIn.searchResultFunction(jwttokencheck!!,query).enqueue(object :Callback<List<SearchModelClass>>{
            override fun onFailure(call: Call<List<SearchModelClass>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<SearchModelClass>>, response: Response<List<SearchModelClass>>) {
                var recyclerView= findViewById<RecyclerView>(R.id.recyclersearchid)
                val searchAdapter = SearchAdapter(sList,this@SearchActivity)
                recyclerView.adapter = searchAdapter
                recyclerView.layoutManager = GridLayoutManager(this@SearchActivity, 2)

                var searchresponse=response.body()!!
                println("reshma${searchresponse}")
                if (searchresponse == null) {
                    Toast.makeText(this@SearchActivity, "No Results", Toast.LENGTH_SHORT).show()
                    return
                }
                else {
                    for (i in searchresponse) {
                        sList.add(SearchModelClass(i.title, i.description, i.price, i.selling_price, i.lat, i.long, i.post_images))
                        println("victor ${sList}")
                    }
                    Log.d("sureka", response.toString())
                    println("suma ${sList}")
                    return
                }

            }

        })

//        retIn.searchFunction(jwttokencheck!!,query).enqueue(object :retrofit2.Callback<List<Model_Home>>{
//            override fun onFailure(call: Call<List<Model_Home>>, t: Throwable) {
//                println("shit")
//            }
//
//            override fun onResponse(call: Call<List<SearchModelClass>>, response: Response<List<Model_Home>>) {
//
//                var searchresponse=response.body()!!
//                println("reshma${searchresponse}")
//
//
//                if (searchresponse == null) {
//                    Toast.makeText(this@SearchActivity, "No Results", Toast.LENGTH_SHORT).show()
//                    return
//                }
//                else {
//                    for (item in searchresponse) {
//                        sList.add(SearchModelClass(item.title, item.description, item.price, item.selling_price, item.lat, item.long, item.post_images))
//                    }
//                    Log.d("sureka", response.toString())
//                    println("suma ${sList}")
//                }
//
////                for(i in searchresponse ){
////                    contacts.add(SearchAsYouTypeModel(i.title,i.title))
////                }
////
////                var adapter = Adapter(contacts, this@SearchtypeActivity)
////                recyclerView.setAdapter(adapter);
////                adapter.notifyDataSetChanged();
//
//
//            }
//        })



    }


}

