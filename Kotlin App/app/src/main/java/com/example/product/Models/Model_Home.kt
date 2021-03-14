package com.example.product.Models

import kotlin.collections.ArrayList

data class Model_Home(
        val id: String,
        val title: String,
        val description: String,
        val price: Int,
        val selling_price: Int,
        val city: String,
//        val lat : Double,
//        val long:Double,
    //    val location: Objects,
        val post_images:  ArrayList<String>,
        val postid:String
)

data class Response(val message : String,val postid:String)
