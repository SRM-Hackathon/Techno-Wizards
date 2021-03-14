package com.example.product


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.product.Api.ApiService
import com.example.product.Api.RetrofitInstance
import com.example.product.Models.Response
import com.example.product.Models.UrlCity
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.create
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.io.File


class PostActivity : AppCompatActivity() {

    private val SHARED_PREF_NAME = "pref"
    private val KEY_JWT = "jwt"
    var latitude: String? = null
    var longitude: String? = null
    var MY_PERMISSIONS_REQUEST = 100
    var PICK_IMAGE_FROM_GALLERY = 1
    var fileUris = ArrayList<Uri>()
    lateinit var selectedImage:ImageView
    lateinit var closebtn:ImageButton
    val postData = HashMap<String, Any>()
    lateinit var parentLinearLayout:LinearLayout
    var rowlist=ArrayList<Any>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
     //   findViewById<View>(R.id.scroll).visibility = View.GONE
        parentLinearLayout=findViewById(R.id.parent_linear_layout)



        if (ContextCompat.checkSelfPermission(this@PostActivity, Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this@PostActivity, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST)
        }


        var sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        var jwttokencheck = sharedPreferences?.getString(KEY_JWT, null)



        var ptitle: TextInputEditText = findViewById(R.id.post_title)
        val postRentPriceEditText: TextInputEditText = findViewById(R.id.post_rentprice)
        val postSellingPriceEditText: TextInputEditText = findViewById(R.id.post_sellingprice)
        val pinCodeTextView: TextInputEditText = findViewById(R.id.post_pincode)
        val descriptionTextView: EditText = findViewById(R.id.Descriptionedittext)



        val uploadButton : Button = findViewById(R.id.upload_btn)


        uploadButton.setOnClickListener {

           // val intent=Intent(Intent.ACTION_GET_CONTENT)
           val intent =Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(Intent.createChooser(intent,"pick image"),PICK_IMAGE_FROM_GALLERY)
        }



        // LOCATION COORDINATES LATITUDE AND LONGITUDE
        val getLocationButton: Button = findViewById(R.id.locationbtn)
        getLocationButton.setOnClickListener {


                GpsUtils.getInstance().findDeviceLocation(this)
                latitude = GpsUtils.getInstance().latitude
                longitude = GpsUtils.getInstance().longitude
                Log.d("Act", "This is Latitude and Long $latitude $longitude")
                Toast.makeText(this, "Location Accessed", Toast.LENGTH_SHORT).show()
                val latitudeValue = latitude.toString()
                val longitudeValue = longitude.toString()
                postData["lat"] = latitudeValue
                postData["lon"] = longitudeValue
                RetrofitInstance.BASE_URL = "https://nominatim.openstreetmap.org/"
                val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
                retIn.getCityName("geocodejson", latitude!!, longitude!!).enqueue(object : Callback<UrlCity>{
                    override fun onResponse(call: Call<UrlCity>, response: retrofit2.Response<UrlCity>) {
                        //Set Current Location Button Response with city lat long
                        postData["city"] = response.body()!!.features[0].properties.geocoding.admin.level6
                        println("POSTDATA on Location Button ${postData}")
                    }

                    override fun onFailure(call: Call<UrlCity>, t: Throwable) {
                    }

                })
        }


        val saveButton: Button = findViewById(R.id.save_button)
        saveButton.setOnClickListener {

            val postTitleValue = ptitle.text.toString()
            val postRentPriceValue = postRentPriceEditText.text.toString()
            val postSellingPriceValue = postSellingPriceEditText.text.toString()
            val pinCodeValue = pinCodeTextView.text.toString()
            val postDescriptionValue = descriptionTextView.text.toString()


            postData["title"] = postTitleValue
            postData["description"] = postDescriptionValue
            postData["price"] = postRentPriceValue
            postData["selling_price"] = postSellingPriceValue
            postData["pincode"] = pinCodeValue

            println("POSTDATA in Save function ${postData}")

            RetrofitInstance.BASE_URL = "http://rentapp.ddns.net:3000"
            val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
            retIn.createNewPost(jwttokencheck!!, postData).enqueue(object : Callback<Response> {
                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {

                    Log.d("IamHere", "Are you there ${response.body()!!.postid}")
                    Toast.makeText(this@PostActivity, "Success ${response.body()!!.postid}", Toast.LENGTH_SHORT).show()
                     var post_id = response.body()!!.postid

                    Log.d("filelist","list is ${fileUris}")

                    uploadImage(fileUris, post_id)


                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Toast.makeText(this@PostActivity, "Failed", Toast.LENGTH_SHORT).show()

                }

            })



        }



    }



    private fun addImage() {
        var inflater:LayoutInflater= getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View =inflater.inflate(R.layout.image,null)
        rowlist.add(rowView)
        Log.d("rosy","$rowlist")

        //ADDING CHILD VIEW TO THE PARENTLAYOUT VIEW
        parentLinearLayout.addView(rowView,parentLinearLayout.childCount-1)
        Log.d("parentcheck","${parentLinearLayout.childCount-1}")
        parentLinearLayout.isFocusable

         selectedImage=rowView.findViewById(R.id.number_edit_text)

       var closebtn=rowView.findViewById<ImageButton>(R.id.img_close_btn)
        closebtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // Do some work here
                Toast.makeText(this@PostActivity,"reached onclick",Toast.LENGTH_SHORT).show()
                Log.d("jack","$view")


              Log.d("godzilla","${parentLinearLayout.indexOfChild(rowView)},$fileUris")

                //UPDATING THE LIST AFTER THE CLOSE BUTTON PRESSED
                fileUris.removeAt(parentLinearLayout.indexOfChild(rowView))
                rowView.visibility=View.GONE
                parentLinearLayout.removeView(rowView)

                Log.d("jd","$fileUris")

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && data!= null && data.data != null) {
            addImage()
            val intentdata=data.data

            val bitimg: Bitmap = ImageUtils.getInstant().getCompressedBitmap(FileUtils.getPath(this,intentdata))
            selectedImage.setImageBitmap(bitimg)
            Log.d("kong"," $bitimg")


            fileUris.add(getImageUri(this,bitimg))

            Log.d("listy","lists ${fileUris}")
        }
    }


    private fun getImageUri(postActivity: PostActivity, bitmap: Bitmap): Uri {
        var byte:ByteArrayOutputStream= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,0,byte)
        var path=MediaStore.Images.Media.insertImage(postActivity.contentResolver,bitmap,"Rentapp",null)
        return Uri.parse(path)
    }

    fun uploadImage(fileUris: ArrayList<Uri>,post_id:String) {
          val SHARED_PREF_NAME = "pref"
          val KEY_JWT = "jwt"

          val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)

          var sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, AppCompatActivity.MODE_PRIVATE)
          var jwttokencheck = sharedPreferences?.getString(KEY_JWT, null)


         val parts : ArrayList<MultipartBody.Part> = ArrayList<MultipartBody.Part>()

        for(uri in fileUris){
            parts.add(prepareFilePart("post_images",uri))
        }


          var imageFile = "post_images"
          val requestImageFile = imageFile.toRequestBody("image/*".toMediaTypeOrNull())

          var postid=post_id

          Log.d("upload","${parts},$post_id")

          retIn.uploadPostPic(jwttokencheck!! , parts, requestImageFile,postid).enqueue(object : Callback<ResponseBody> {
              override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                  Toast.makeText(this@PostActivity, "Working", Toast.LENGTH_SHORT).show()
              }

              override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                  Toast.makeText(this@PostActivity, "NOt Working", Toast.LENGTH_SHORT).show()
              }

          })


        var intent = Intent(this,MainActivity::class.java)
        intent.putExtra("PushToProfileFlag",1)
        startActivity(intent)


    }


    fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
      val file: File = com.example.product.FileUtils.getFile(this, fileUri)

        // create RequestBody instance from file
        val requestFile: RequestBody = create(
                contentResolver.getType(fileUri)!!.toMediaTypeOrNull(),file
        )

        return MultipartBody.Part.Companion.createFormData(partName, file.name, requestFile)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{

                }
                return;

            }
        }

    }

    override fun onBackPressed() {
        var intent:Intent=Intent(this,MainActivity::class.java)
        startActivityForResult(intent,1)
        finish()
    }




}

