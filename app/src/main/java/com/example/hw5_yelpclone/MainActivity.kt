package com.example.hw5_yelpclone

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        val displayLimit = 30 //how many items to display in recycler view (default is 20, max is 50) Displaying too much can cause irrelevant results to be shown for search

        if (food.isEmpty() || location.isEmpty()){
            showDialog2(view)
        }else{
            restaurant.clear();
            yelpService.searchRestaurants("Bearer $API_KEY",food,displayLimit, location).enqueue(object : Callback<YelpSearchResult> {
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
        }
        foodbar.hideKeyboard()
        locationbar.hideKeyboard()
    }

    fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
    fun showDialog2(view: View) {

        // Create an alertdialog builder object,
        // then set attributes that you want the dialog to have
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Search term missing")
        builder.setMessage("Search term cannot be empty, Please enter a search term!")
        // Set an icon, optional
        builder.setIcon(android.R.drawable.ic_delete)

        // Set the button actions, all of them are optional
        builder.setPositiveButton("OKAY"){ dialog, which ->
            // code to run when YES is pressed
        }

        // create the dialog and show it
        val dialog = builder.create()
        dialog.show()
    }
}
