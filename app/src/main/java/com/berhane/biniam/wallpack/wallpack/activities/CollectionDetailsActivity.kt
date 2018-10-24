/*
 * DayTime:9/25/18 1:21 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.View.frag.CollectionDetailsFragment
import com.berhane.biniam.wallpack.wallpack.View.frag.CollectionFragment
import com.berhane.biniam.wallpack.wallpack.model.data.PhotoCollection
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson


class CollectionDetailsActivity : AppCompatActivity() {

    private val mContext: Context? = null
    private val TAG: String = "CollectionDActivity"
    private val manager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.collection_details_activity)
        // Getting the Photo Collection data from Json
        val collectionPhotos = Gson().fromJson<PhotoCollection>(intent.getStringExtra("collection"), PhotoCollection::class.java)
        // view setting here
        val collectionDescription = findViewById<TextView>(R.id.collectionDescription_tv)
        val activityCollectionTitle = findViewById<TextView>(R.id.activity_collection_title)
        val photographerImg = findViewById<ImageView>(R.id.collectionPhotographerImg)
        val photographerCollection = findViewById<TextView>(R.id.photographerCollection)
        val collectionTopCover = findViewById<ImageView>(R.id.collection_topCover)

        // load the image here
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(this, R.color.tokyoColorAccent))
        circularProgressDrawable.start()

        activityCollectionTitle.text = collectionPhotos.title
        collectionDescription.text = collectionPhotos.description
        val requestOption = RequestOptions().placeholder(circularProgressDrawable).centerCrop()
        photographerCollection.text = "By\t" + collectionPhotos.user.name


        Glide.with(this@CollectionDetailsActivity)
                .load(collectionPhotos.cover_photo.urls.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(collectionTopCover)

        Glide.with(this@CollectionDetailsActivity)
                .load(collectionPhotos.user.profile_image.medium)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOption)
                .into(photographerImg)


        if (collectionPhotos.curated) {
            val curatedPhotoCollection = CollectionFragment.newInstance(PhotoConstants.COLLECTION_TYPE_CURATED)
            loadFragment(curatedPhotoCollection)
        } else {
            val newPhotoFragment = CollectionDetailsFragment.newInstance(collectionPhotos)
            loadFragment(newPhotoFragment)
        }
        // On Click even on the Photographer photo to see the details of the photographer
        photographerImg.setOnClickListener {
            val i = Intent(this, PhotoDetails::class.java)
            i.putExtra("Photo", Gson().toJson(collectionPhotos))
            startActivity(i)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.collectionFragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}