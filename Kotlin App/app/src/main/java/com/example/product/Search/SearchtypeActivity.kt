package com.example.product.Search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.product.Api.ApiService
import com.example.product.Api.RetrofitInstance
import com.example.product.Models.Model_Home
import com.example.product.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


class SearchtypeActivity : AppCompatActivity() {


    var product=ArrayList<String>()
    var queryTextChangedJob : Job? = null
    var contacts=ArrayList<SearchAsYouTypeModel>()
    private lateinit var recyclerView: RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_layout)
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = LinearLayoutManager(this);
        recyclerView.layoutManager=layoutManager


        var searchView = findViewById<View>(R.id.actv) as SearchView
        var imageButton = findViewById<View>(R.id.back_btn) as ImageButton
        searchView.isIconifiedByDefault = true
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()


        Log.d("shakeey","${product}")

//        listView.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
//            val query = listView.getItemAtPosition(i).toString()
//            val intent = Intent(this@SearchtypeActivity, SearchActivity::class.java)
//            intent.putExtra("query", query)
//            startActivity(intent)
//        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                val `in` = Intent(this@SearchtypeActivity, SearchActivity::class.java)
                `in`.putExtra("query", searchView.query.toString())
                var typetext=searchView.query.toString()
                println("orgasm $typetext")
                startActivity(`in`)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                queryTextChangedJob?.cancel()
                queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main){
                    println("Your Life cycler Started...")
                    delay(200)
                    //Perform Search Function

                    searchOnRetroFit(s)
                    print("Async Function Working fine and Great")

                }
                if (s.isEmpty()) {
                    recyclerView.visibility = View.INVISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                }
//                println("jacky ${product}")
//                listView.adapter = arrayAdapter
//                arrayAdapter.filter.filter(s)
//                arrayAdapter.notifyDataSetChanged()
//                if (s.isEmpty()) {
//                    listView.visibility = View.INVISIBLE
//                } else {
//                    listView.visibility = View.VISIBLE
//                }
                return false
            }
        })
        imageButton.setOnClickListener { finish() }
    }
    private fun searchOnRetroFit(query : String) {
        val SHARED_PREF_NAME="pref"
        val KEY_JWT="jwt"
        var sharedPreferences= getSharedPreferences(SHARED_PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        var jwttokencheck = sharedPreferences?.getString(KEY_JWT,null)

        RetrofitInstance.BASE_URL="http://rentapp.ddns.net:3000"
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        retIn.searchFunction(jwttokencheck!!,query).enqueue(object :retrofit2.Callback<List<Model_Home>>{
            override fun onFailure(call: Call<List<Model_Home>>, t: Throwable) {
              println("shit")
            }

            override fun onResponse(call: Call<List<Model_Home>>, response: Response<List<Model_Home>>) {
             println("sunnyleone ${response.body()}")
                var searchresponse=response.body()!!

                for(i in searchresponse ){
                    contacts.add(SearchAsYouTypeModel(i.title,i.title))
                }

               var adapter = Adapter(contacts, this@SearchtypeActivity)
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }
        })
        contacts.clear()


    }
}