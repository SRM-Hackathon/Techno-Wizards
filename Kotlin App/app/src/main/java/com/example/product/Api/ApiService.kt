package com.example.product.Api

import com.example.product.Models.*
import com.example.product.Search.SearchModelClass
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("/api/post/home")
    fun fetchHomePost(@Header("auth-token") auth:String, @Query("lat") lat: Double, @Query("lon") lon: Double) : Call<List<Model_Home>>

    @GET("/api/post/mypost")
    fun fetchMyPost(@Header("auth-token") auth:String) : Call<List<Model_Home>>

    @POST("/api/store/newstore")
    fun postStoreName(@Body city:String,storename:String,@Query("lat") lat: Double, @Query("lon") lon: Double):Call<Response>

    @POST("/api/user/sendotp")
    fun getOtpFromServer(@Body email : HashMap<String , String>) : Call<OTP>

    @POST("/api/user/verify")
    fun verifyOtpFromServer(@Body verify : HashMap<String , Any>) : Call<JWT>

    @HTTP(method = "DELETE", path = "/api/post/deletemypost", hasBody = true)
    fun deleteMyPost(@Header("auth-token") auth : String, @Body options : HashMap<String, String>) :Call<Response>

    @POST("/api/post/new_post")
    fun createNewPost(@Header("auth-token") auth : String , @Body post : HashMap<String, Any>): Call<Response>

    @PATCH("/api/post/editpost")
    fun editMyPost(@Header("auth-token") auth : String ,@Body edit:HashMap<String,Any>): Call<Response>

    @GET("api/post/search")
    fun searchFunction(@Header("auth-token") auth: String,@Query("q") query : String) : Call<List<Model_Home>>

    @GET("api/post/search")
    fun searchResultFunction(@Header("auth-token") auth: String,@Query("q") query : String) : Call<List<SearchModelClass>>

    @Multipart
    @POST("api/post/upload_post")
    fun uploadPostPic(@Header("auth-token") auth : String, @Part image: List<MultipartBody.Part>, @Part("post_images") name : RequestBody,@Query("postid") postid : String) : Call<ResponseBody>
    @GET("reverse")
    fun getCityName(@Query("format") format : String, @Query("lat") lat: String, @Query("lon") lon : String) : Call<UrlCity>

    @GET("json")
    fun getCoordinate(@Query("") coordinate:String):Call<IpReturnType>






    //Reference:-
    @GET("/api/post/allpost")
    fun fetchAllThePost() : Call<List<Model_Home>>




}




class RetrofitInstance {
    companion object {
        var BASE_URL: String?= null

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
    }
}
