package com.example.hw5_yelpclone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://api.yelp.com/v3/"
    private val TAG = "MainActivity"
    private val API_KEY = "7TLF-WenYVxYvHhwptrneNoOpcCxym8usscb1zUYWuTurOKAd1NI-pecwl7_cpRNAF5HW511BBSzQ0-LDNtyTbfx18cnPKcZSnHbPywUVdApf7mp7OfDveulF8SUXnYx"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        restaurantRecyler.adapter = adapter
        restaurantRecyler.layoutManager = LinearLayoutManager(this)

    }

    val restaurant = mutableListOf<YelpRestaurant>()
    val adapter = RestaurantsAdapter(this, restaurant)

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val yelpService = retrofit.create(YelpService::class.java)

    fun search(view: View){
        val food = searchFood.text.toString()
        val location = searchLocation.text.toString()
        val foodbar = findViewById<EditText>(R.id.searchFood)
        val locationbar = findViewById<EditText>(R.id.searchLocation)

        yelpService.searchRestaurants("Bearer $API_KEY",food, location).enqueue(object : Callback<YelpSearchResult> {
            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                Log.i(TAG, "onResponse $response")
                val body = response.body()
                if (body == null){
                    Log.w(TAG, "Did not receive anything from Yelp Api")
                    return
                }
                restaurant.addAll(body.restaurants)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.i(TAG, "onFailure $t")
            }
        })
        foodbar.hideKeyboard()
        locationbar.hideKeyboard()
    }

    fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}
