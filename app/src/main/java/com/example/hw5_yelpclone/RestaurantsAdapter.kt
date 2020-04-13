package com.example.hw5_yelpclone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_restaurant.view.*

class RestaurantsAdapter(val context: Context, val restaurant: List<YelpRestaurant>) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false))
    }

    override fun getItemCount() = restaurant.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurants = restaurant[position]
        holder.bind(restaurants)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(restaurants: YelpRestaurant) {
            itemView.restaurantName.text = restaurants.name
            itemView.ratingBar2.rating = restaurants.rating.toFloat()
            itemView.numReviews.text = "${restaurants.numReviews} Reviews"
            itemView.address.text = restaurants.location.address
            itemView.category.text = restaurants.categories[0].title
            itemView.distance.text = restaurants.displayDistance()
            itemView.price.text = restaurants.price
            Glide.with(context).load(restaurants.imageUrl).apply(RequestOptions().transform(CenterCrop())).into(itemView.imageView2)
        }

    }
}
