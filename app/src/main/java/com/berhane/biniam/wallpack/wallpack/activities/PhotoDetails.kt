/*
 * DayTime:9/20/18 9:10 AM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import java.text.NumberFormat
import java.util.*

class PhotoDetails : AppCompatActivity() {

    var context: Context? = null
    val TAG = "PhotoDetails"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_photo)
        context = this

        val imageView = findViewById<ImageView>(R.id.image_detailed)
        val photographerImg = findViewById<ImageView>(R.id.photographerIMGView)
        val photographerName = findViewById<TextView>(R.id.photographerName)
        val like_details_btn = findViewById<ImageButton>(R.id.like_details_btn)
        val detailed_user_location = findViewById<TextView>(R.id.detailed_user_location)
        val published_date = findViewById<TextView>(R.id.published_date)
        val likes_details = findViewById<TextView>(R.id.likes_details)
        val details_download = findViewById<TextView>(R.id.details_download)
        val color_details = findViewById<TextView>(R.id.color_details)


        val photos: Photos = Gson().fromJson<Photos>(intent.getStringExtra("Photo"), Photos::class.java)

        photographerName.text = "By " + photos.user.first_name
        detailed_user_location.text = photos.user.location
        published_date.text = photos.created_at.split("T")[0]
        likes_details.text=  NumberFormat.getInstance(Locale.US).format(photos.likes)
       // details_download.text=photos.
        color_details.text=photos.color

        // load the image here
        Glide.with(this@PhotoDetails)
                .load(photos.urls.small)
                .apply(RequestOptions().priority(Priority.HIGH)
                        .placeholder(R.drawable.ic_placeholder))
                .into(imageView)


        Glide.with(this@PhotoDetails)
                .load(photos.user.profile_image.medium)
                .apply(RequestOptions().priority(Priority.HIGH)
                        .placeholder(R.drawable.ic_account_circle))
                .into(photographerImg)


    }

}