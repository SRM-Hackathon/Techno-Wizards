package com.example.product

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.product.Api.ApiService
import com.example.product.Api.RetrofitInstance
import com.example.product.Models.Response
import com.example.product.Models.UrlCity
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception


class EditActivity : AppCompatActivity() {
    private var images:ArrayList<Uri?>?=null
    private var position=0;
    private val PICK_IMAGES_CODE=0;
    private val SHARED_PREF_NAME = "pref"
    private val KEY_JWT = "jwt"
    var latitude: String? = null
    var longitude: String? = null
    var MY_PERMISSIONS_REQUEST = 100
    var PICK_IMAGE_FROM_GALLERY = 1
    var fileUris = ArrayList<Uri>()
    var fileUris1 = ArrayList<Uri>()
    lateinit var selectedImage:ImageView
    val editpostData = HashMap<String, Any>()
    lateinit var parentLinearLayout:LinearLayout
    var rowlist=ArrayList<Any>()
    var samplemultipart: ArrayList<MultipartBody.Part> = ArrayList<MultipartBody.Part>()

    var bitmap: ArrayList<Bitmap>?=null
    companion object {

        var shit=ArrayList<Uri>()

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        parentLinearLayout=findViewById(R.id.parent_linear_layout)
        images= ArrayList()


        var sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        var jwttokencheck = sharedPreferences?.getString(KEY_JWT, null)


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
            editpostData["lat"] = latitudeValue
            editpostData["long"] = longitudeValue
            RetrofitInstance.BASE_URL = "https://nominatim.openstreetmap.org/"
            val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
            retIn.getCityName("geocodejson", latitude!!, longitude!!).enqueue(object : Callback<UrlCity>{
                override fun onResponse(call: Call<UrlCity>, response: retrofit2.Response<UrlCity>) {
                    //Set Current Location Button Response with city lat long
                    editpostData["city"] = response.body()!!.features[0].properties.geocoding.admin.level6
                    println("POSTDATA on Location Button ${editpostData}")
                }

                override fun onFailure(call: Call<UrlCity>, t: Throwable) {
                }

            })

        }

        val eId=intent.getStringExtra("_id")
        val epostId=intent.getStringExtra("postid")

        val editTitleText: TextInputEditText = findViewById(R.id.edit_title)
        val etitle=intent.getStringExtra("etitle")
        editTitleText.setText(etitle, TextView.BufferType.EDITABLE)

        val editrentprice: TextInputEditText = findViewById(R.id.edit_rentprice)
        val erentprice=intent.getIntExtra("erentprice",0)
        editrentprice.setText(erentprice.toString(), TextView.BufferType.EDITABLE)

        val editsellinprice: TextInputEditText = findViewById(R.id.edit_sellingprice)
        val esellingprice=intent.getIntExtra("esellingprice",0)
        editsellinprice.setText(esellingprice.toString(), TextView.BufferType.EDITABLE)

        val editdescription:EditText = findViewById(R.id.edit_Descriptionedittext)
        val edescription=intent.getStringExtra("edescription")
        editdescription.setText(edescription)

        val eimageList= intent.getSerializableExtra("postimages") as ArrayList<String>

        val elat=intent.getStringExtra("lat")

        val elong=intent.getStringExtra("long")

        val eCity=intent.getStringExtra("city")

       // var ebitlist=intent.getSerializableExtra("bitlist") as ArrayList<Bitmap>
        val saveButton: Button = findViewById(R.id.save_button)
        saveButton.setOnClickListener {

//            val editpostTitleValue =etitle!!
//            val editpostRentPriceValue = erentprice.toString()
//            val editpostSellingPriceValue = esellingprice.toString()
//            // val editpinCodeValue = pinCodeTextView.text.toString()
//            val editpostDescriptionValue = edescription!!
//            val editpostLatValue=elat!!
//            val editpostLongValue=elong!!
//            val editpostCityValue=eCity!!
//            val _id=eId!!
//            val editpostIdValue=epostId!!

            val editpostTitleValue =editTitleText.text.toString()
            val editpostRentPriceValue =  editrentprice.text.toString()
            val editpostSellingPriceValue = editsellinprice.text.toString()
            // val editpinCodeValue = pinCodeTextView.text.toString()
            val editpostDescriptionValue = editdescription.text.toString()
            val editpostLatValue=elat!!
            val editpostLongValue=elong!!
            val editpostCityValue=eCity!!
            val _id=eId!!
            val editpostIdValue=epostId!!



            editpostData["_id"]=_id
            editpostData["postid"]=editpostIdValue
            editpostData["title"] = editpostTitleValue
            editpostData["description"] = editpostDescriptionValue
            editpostData["price"] = editpostRentPriceValue
            editpostData["selling_price"] = editpostSellingPriceValue
            editpostData["lat"]=editpostLatValue
            editpostData["long"]=editpostLongValue
            editpostData["pincode"]="600000"
            // editpostData["city"]=editpostCityValue
            editpostData["city"]="qwertyuiopasdfghjklzxcvbnmqwertyuiopasfghjk"


            //  editpostData["pincode"] = pinCodeValue

            println("POSTDATA in Save function ${editpostData},${_id}")

            RetrofitInstance.BASE_URL = "http://rentapp.ddns.net:3000"
            val retIn = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
            retIn.editMyPost(jwttokencheck!!,editpostData).enqueue(object :Callback<Response>{
                override fun onFailure(call: Call<Response>, t: Throwable) {

                }

                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                    Log.d("IamHere", "Are you there ${response.body()!!.postid}")
                    Toast.makeText(this@EditActivity, "Success ${response.body()!!.postid}", Toast.LENGTH_SHORT).show()
                    var post_id = response.body()!!.postid
                    Log.d("cramp","list is ${samplemultipart}")
                    uploadImage(fileUris, post_id)
                }

            })
        }


        for(i in 0..eimageList.size-1){
            addImage()
            println("boom ${eimageList}")
            Glide.with(this)
                    .load(eimageList[i])
                    .into(selectedImage)
            Picasso.get()
                    .load(eimageList[i])
                    .into(object:Target{
                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            println("navel $fileUris")

                        }

                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

                        }

                        override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?){

                            fileUris.add(getImageUri(this@EditActivity,bitmap!!))
                            println("ditch $fileUris")


                        }
                    })


        }
      //  samplemultipart =postImage(eimageList)

    //    println("busty $samplemultipart")
        println("busty $fileUris")
    }

    private fun addImage() {
        var inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
                Toast.makeText(this@EditActivity,"reached onclick",Toast.LENGTH_SHORT).show()
                Log.d("jack","$view")


                Log.d("godzilla","${parentLinearLayout.indexOfChild(rowView)},$fileUris")

                //UPDATING THE LIST AFTER THE CLOSE BUTTON PRESSED
               // samplemultipart.removeAt(parentLinearLayout.indexOfChild(rowView))
                fileUris.removeAt(parentLinearLayout.indexOfChild(rowView))
                rowView.visibility= View.GONE
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
           // intentDataImage(getImageUri(this,bitimg))

            Log.d("listy","lists ${samplemultipart}")
        }
    }
    private fun getImageUri(editActivity: EditActivity, bitmap: Bitmap): Uri {
        var byte: ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,0,byte)
        var path= MediaStore.Images.Media.insertImage(editActivity.contentResolver,bitmap,"Rentapp",null)
        return Uri.parse(path)
    }

//    fun intentDataImage(file:Uri){
//        samplemultipart.add(prepareFilePart("post_images",file))
//    }

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

    //    Log.d("upload","${samplemultipart},$post_id")
      Log.d("upload","${parts},$post_id")


        retIn.uploadPostPic(jwttokencheck!! ,parts, requestImageFile,postid).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                Toast.makeText(this@EditActivity, "Working", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@EditActivity, "NOt Working", Toast.LENGTH_SHORT).show()
                println("showy ${t.localizedMessage}")
            }

        })


        var intent = Intent(this,MainActivity::class.java)
        intent.putExtra("PushToProfileFlag",1)
        startActivity(intent)

    }
    //    private fun postImage(selectedUris: java.util.ArrayList<String>):
//            ArrayList<MultipartBody.Part>  {
//        var multiParts: ArrayList<MultipartBody.Part> = ArrayList<MultipartBody.Part>()
//        for (i in 0 until selectedUris.size) {
//            // 1. Create File using image url (String)
//            val file = File(selectedUris.get(i))
//            // 2. Create requestBody by using multipart/form-data MediaType from file
//            val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//            // 3. Finally, Create MultipartBody using MultipartBody.Part.createFormData
//            val body: MultipartBody.Part = MultipartBody.Part.createFormData(
//                    "image", file.name.trim(), requestFile)
//            multiParts.add(body)
//            println("nithyanandha $multiParts")
//        }
//        return multiParts
//    }
//    private fun postImage(selectedUris: java.util.ArrayList<String>):
//            ArrayList<MultipartBody.Part>  {
//        var multiParts: ArrayList<MultipartBody.Part> = ArrayList<MultipartBody.Part>()
//        for (i in 0 until selectedUris.size) {
//            // 1. Create File using image url (String)
//            val file = File(selectedUris.get(i))
//            var u=Uri.fromFile(file)
//            var newfile=FileUtils.getFile(this,u)
//            // 2. Create requestBody by using multipart/form-data MediaType from file
//            val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//            // 3. Finally, Create MultipartBody using MultipartBody.Part.createFormData
//            val body: MultipartBody.Part = MultipartBody.Part.createFormData(
//                    "image", file.name.trim(), requestFile)
//            multiParts.add(body)
//            println("nithyanandha $multiParts, $u,$newfile")
//        }
//        return multiParts
//    }
    fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file: File = com.example.product.FileUtils.getFile(this, fileUri)
        var uload=Uri.fromFile(file)
        println("uload $fileUri")

        // create RequestBody instance from file
        val requestFile: RequestBody = RequestBody.create(
                contentResolver.getType(fileUri)!!.toMediaTypeOrNull(), file
        )

        return MultipartBody.Part.Companion.createFormData(partName, file.name, requestFile)
    }

}