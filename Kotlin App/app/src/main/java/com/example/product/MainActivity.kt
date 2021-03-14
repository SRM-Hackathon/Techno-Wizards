package com.example.product

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.product.Api.ApiService
import com.example.product.Api.RetrofitInstance
import com.example.product.Home.Home
import com.example.product.Models.JWT
import com.example.product.Models.OTP
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    val verifyHashMap = HashMap<String, Any>()

    private val SHARED_PREF_NAME="pref"
    private val KEY_JWT="jwt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        FirebaseMessaging.getInstance().subscribeToTopic("general")

//        FirebaseInstanceId.getInstance().instanceId
//                .addOnSuccessListener(object : OnSuccessListener<InstanceIdResult> {
//                    override fun onSuccess(instanceIdResult: InstanceIdResult) {
//                        val token = instanceIdResult.token //Token
//                        Log.d("ID",token)
//
//                    }
//                })

        var sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE)

        //check sharedpreferenced for jwt token
        var jwttokencheck=sharedPreferences.getString(KEY_JWT,null)
        println("JWT ${jwttokencheck}")

        val dflag=intent.getIntExtra("dflag",0)
        println("domor $dflag")

        if(jwttokencheck != null &&dflag==0){
            findViewById<View>(R.id.stageOne).visibility = View.GONE
            findViewById<View>(R.id.stageTwo).visibility = View.GONE
            findViewById<View>(R.id.stageThree).visibility = View.VISIBLE
            setFragment(Home())
        }
        else if(jwttokencheck != null && dflag==1 ){
            findViewById<View>(R.id.stageOne).visibility = View.GONE
            findViewById<View>(R.id.stageTwo).visibility = View.GONE
            findViewById<View>(R.id.stageThree).visibility = View.VISIBLE

            //DIRECTING TO PROFILE PAGE AFTER ONE POST IS DELETED

            var  btnavViewId = findViewById<BottomNavigationView>(R.id.bottomnavigationviewid)
            var toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            setFragment(Profile())
            toolbar.title = "Profile"
            btnavViewId.menu.findItem(R.id.notificationsid).setChecked(true )
        }
        else{
            findViewById<View>(R.id.stageOne).visibility = View.VISIBLE
            findViewById<View>(R.id.stageTwo).visibility = View.GONE
            findViewById<View>(R.id.stageThree).visibility = View.GONE
        }

       // GpsUtils.getInstance().findDeviceLocation(this)


        val otpEditText = findViewById<EditText>(R.id.otpverfication)
        val stage = IntArray(1)
        val emailValidate = findViewById<EditText>(R.id.emailValidate)
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        stage[0] = 1
        val opt_page_btn = findViewById<Button>(R.id.nxt_otppage)
        val home_page_btn=findViewById<Button>(R.id.otpbtn)

        opt_page_btn.setOnClickListener {
            if (ValidateEmailAddress(emailValidate)) {
                findViewById<View>(R.id.stageOne).visibility = View.GONE
                findViewById<View>(R.id.stageTwo).visibility = View.VISIBLE

                stage[0]++
            }
        }

        home_page_btn.setOnClickListener {

            val otpValue = otpEditText.text.toString()
            verifyHashMap["otp"] = otpValue.toInt()
            Log.d("JWT", "This is JWT ${verifyHashMap}")
            RetrofitInstance.BASE_URL = "http://185.216.25.160:5000"
            val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
            retIn.verifyOtpFromServer(verifyHashMap).enqueue(object : Callback<JWT> {
                override fun onResponse(call: Call<JWT>, response: Response<JWT>) {

                    var editor: SharedPreferences.Editor = sharedPreferences.edit()
                    var jwttoken = response.body()!!.message
                    editor.putString(KEY_JWT, jwttoken)
                    editor.apply()

                    Log.d("ActiJWt", "This is JwT ${response.body()!!.message}")

                    //      if (ValidateEmailAddress(emailValidate)) {
//                    if(jwttoken==null){
//                        findViewById<View>(R.id.stageOne).visibility = View.GONE
//                        findViewById<View>(R.id.stageTwo).visibility = View.VISIBLE
//                        findViewById<View>(R.id.stageThree).visibility = View.GONE
//                        findViewById<View>(R.id.stageFour).visibility = View.GONE
//
//                    }

                    findViewById<View>(R.id.stageOne).visibility = View.GONE
                    findViewById<View>(R.id.stageTwo).visibility = View.GONE
                    findViewById<View>(R.id.stageThree).visibility = View.VISIBLE
                    findViewById<View>(R.id.stageFour).visibility = View.GONE
                    stage[0]++
                    // setFragment(Home())

                    //       }


                }

                override fun onFailure(call: Call<JWT>, t: Throwable) {
                    Toast.makeText(applicationContext, "Email Invalid", Toast.LENGTH_LONG).show()
                }

            })
        }
        RetrofitInstance.BASE_URL = "http://185.216.25.160:5000"
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
            GpsUtils.getInstance().findDeviceLocation(this@MainActivity)
            var lati = GpsUtils.getInstance().latitude.toDouble()
            var longi = GpsUtils.getInstance().longitude.toDouble()
            Log.d("Act", "This is Latitude and Long $lati $longi")
            var storename=findViewById<EditText>(R.id.storename)
           var cityname=findViewById<EditText>(R.id.cityname)

            var storenamevalue=storename.text.toString()
            var citynamevalue=cityname.text.toString()

            retIn.postStoreName(citynamevalue,storenamevalue,lati,longi).enqueue(object:Callback<com.example.product.Models.Response>{
                override fun onFailure(call: Call<com.example.product.Models.Response>, t: Throwable) {
                    Toast.makeText(this@MainActivity,"wasteed",Toast.LENGTH_SHORT).show()

                }

                override fun onResponse(call: Call<com.example.product.Models.Response>, response: Response<com.example.product.Models.Response>) {
                   Log.d("bhavani","${response.body()!!}")
                }

            })

setFragment(Home())


        var  btnavViewId = findViewById<BottomNavigationView>(R.id.bottomnavigationviewid)
//        var frameLayout = findViewById<FrameLayout>(R.id.framelayoutid)
        var toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)




            //      DELETION completed FLOW CORRECTED
        //    val dflag=intent.getIntExtra("dflag",0)
//
//        //POST SAVED FLOW CORRECTED
//        val postsaveflag=intent.getIntExtra("PushToProfileFlag",0)
//
//        if(dflag==1){
//
//                    setFragment(Profile())
//                    toolbar.title = "Profile"
//                    btnavViewId.menu.findItem(R.id.notificationsid).setChecked(true )

   //     }
//
//        else if(postsaveflag==1){
//                setFragment(Profile())
//                 toolbar.title = "Profile"
//                 btnavViewId.menu.findItem(R.id.notificationsid).setChecked(true )
//        }
//        else{
//            setFragment(Home())
//        }

        // starting of home frag while opening apk
        btnavViewId.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            val items = menuItem.itemId
            when (items) {
                R.id.homeid -> {
                    setFragment(Home())
                    toolbar.title = "Home"
                    true
                }
                R.id.dashboardid -> {
                    val intent = Intent(this ,  PostActivity::class.java)
                    startActivity(intent)
                    btnavViewId.menu.findItem(R.id.notificationsid).setChecked(false)
                    //toolbar.title = "Post"
                    true
                }
                R.id.notificationsid -> {
                    setFragment(Profile())
                    toolbar.title = "Profile"
                    true
                }
                R.id.productid->{
                    setFragment(Products())
                    toolbar.title="Products"
                    true
                }
                else -> false
            }
        })
    }

    private fun ValidateEmailAddress(emailValidate: EditText): Boolean {
        val email = emailValidate.editableText.toString().trim { it <= ' ' }

        return if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            val emailJson = HashMap<String , String>()
            emailJson["email"] = email

            Log.d("Jsoin", "afasfa ${emailJson}")

            //Send email to API
            RetrofitInstance.BASE_URL = "http://185.216.25.160:5000/"
            val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)

            retIn.getOtpFromServer(emailJson).enqueue(object : Callback<OTP> {
                override fun onResponse(call: Call<OTP>, response: Response<OTP>) {
                    Log.d("Resss", "I am not here ${response.body()!!}")
                    verifyHashMap["id"] = response.body()!!.sessionid
                    Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                    Log.d("JWT" , "I am is JWT ${verifyHashMap}" )

                }

                override fun onFailure(call: Call<OTP>, t: Throwable) {
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }

            })
            true
        } else {
            Toast.makeText(this, "Email Invalid", Toast.LENGTH_LONG).show()
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.chat_id -> Toast.makeText(this, "chat activity", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                var  btnavViewId = findViewById<BottomNavigationView>(R.id.bottomnavigationviewid)
                btnavViewId.menu.findItem(R.id.homeid).setChecked(true )
            }
        }


    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayoutid, fragment)
      // fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        var  btnavViewId = findViewById<BottomNavigationView>(R.id.bottomnavigationviewid)

       if(btnavViewId.selectedItemId==R.id.homeid){
           finishAffinity()
           finish()
       }
        else{
           btnavViewId.selectedItemId=R.id.homeid

       }
    }


}