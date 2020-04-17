package com.example.hw5_yelpclone

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_restaurant.view.*

class RestaurantsAdapter(val context: Context, val restaurant: List<YelpRestaurant>) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>(){

    private val TAG = "adapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false))
    }

    override fun getItemCount() = restaurant.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurants = restaurant[position]
        holder.bind(restaurants)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        // Set onClickListener to show a toast message for the selected row item in the list
        init{
            itemView.setOnClickListener{
                Toast.makeText(itemView.context, "Opening in maps: ${restaurant[adapterPosition].name}",
                    Toast.LENGTH_SHORT).show()

                val Yelplocation = restaurant[adapterPosition].location

                var address = Yelplocation.address //street address
                address = "$address ${Yelplocation.city} ${Yelplocation.state}" //street, city, and state
                val businessName = restaurant[adapterPosition].name.take(15).replace("&","and") //business name from Yelp limited to 15 characters

                val url = "http://maps.google.co.in/maps?q=$businessName$address"  //show location by using a combination of business name and address
                //val url = "http://maps.google.com/maps?daddr=$businessName$address" //directions to address
                //val location = Uri.parse("geo:41.692438,-72.7680165?z=14") //ccsu coordinates

                val location = Uri.parse(url)
                val mapIntent = Intent(Intent.ACTION_VIEW, location)
                context.startActivity(mapIntent)
            }

            // Set onLongClickListener to show a toast message and remove the selected row item from the list
            itemView.setOnLongClickListener {
                Toast.makeText(
                    itemView.context, "Calling: ${restaurant[adapterPosition].name}",
                    Toast.LENGTH_SHORT
                ).show()

                val phoneNumber = restaurant[adapterPosition].phoneNum
                // Dial a phone number
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:$phoneNumber")
                context.startActivity(callIntent)

                return@setOnLongClickListener true
            }
        }

        fun bind(restaurants: YelpRestaurant) {
            itemView.restaurantName.text = restaurants.name
            itemView.ratingBar2.rating = restaurants.rating.toFloat()
            itemView.numReviews.text = "${restaurants.numReviews} Reviews"
            itemView.address.text = restaurants.location.address
            itemView.category.text = restaurants.categories[0].title
            itemView.distance.text = restaurants.displayDistance()
            itemView.price.text = restaurants.price

            if (restaurants.imageUrl ==null || restaurants.imageUrl.isEmpty()){
                Picasso.get().load("https://sciences.ucf.edu/psychology/wp-content/uploads/sites/63/2019/09/No-Image-Available.png").into(itemView.imageView2)
            }
            else {
                Picasso.get().load(restaurants.imageUrl).into(itemView.imageView2)
            }
        }
    }
}
