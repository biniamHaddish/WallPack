/*
 * DayTime:9/13/18 12:07 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.activities.CollectionDetailsActivity
import com.berhane.biniam.wallpack.wallpack.activities.PhotoDetails
import com.berhane.biniam.wallpack.wallpack.model.data.PhotoCollection
import com.berhane.biniam.wallpack.wallpack.utils.WallPack
import com.berhane.biniam.wallpack.wallpack.utils.image_utills.ColorShifter
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.google.gson.Gson
import java.util.*

class CollectionAdapter(collectionPhotos: List<PhotoCollection>, context: Context)
    : RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {


    var collectionPhotoList: List<PhotoCollection> = collectionPhotos
    var context: Context = context
    val TAG: String = "CollectionAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.collection_items, null)
        return CollectionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return collectionPhotoList.size
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, postion: Int) {
        val collectionPhoto = collectionPhotoList[postion]

        val fadeAnimation = ViewPropertyTransition.Animator { view ->
            val fadeAnimation = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
            fadeAnimation.duration = 500// 500 is good for the eye
            fadeAnimation.start()
        }
        //setting the background color of image holder
        holder.frameLayout.setBackgroundColor(ColorShifter.computeCardBackgroundColor(context, collectionPhoto.cover_photo.color))


        //DisplayMetrics
        val displayMetrics = context.resources.displayMetrics
        val imageHeight: Float = displayMetrics.widthPixels / (collectionPhoto.cover_photo.width.toFloat() / collectionPhoto.cover_photo.height.toFloat())
        //Author image
        Glide.with(context)
                .load(collectionPhoto.user.profile_image.medium)
                .into(holder.author_image)
//        // collection Title
        holder.collection_name.text = collectionPhoto.title
//        //Collection Count or size
        holder.collection_count.text = WallPack.applicationContext().resources.getString(R.string.photos, collectionPhoto.total_photos.toString())

        // Loading the Collection Photos rep.
        Glide.with(context)
                .load(collectionPhoto.cover_photo.urls.regular)
                .transition(GenericTransitionOptions.with(fadeAnimation))
                .into(holder.image_preview)
        holder.image_preview.minimumHeight = imageHeight.toInt()


        holder.image_preview.setOnClickListener {
            //Will send the Json data to the Collection details Activity
            val gSon = Gson().toJson(collectionPhoto)
            val i = Intent(context, CollectionDetailsActivity::class.java)
            i.putExtra("collection", Gson().toJson(collectionPhoto))
            context.startActivity(i)
            Log.d(TAG, "Collection Image Clicked... $gSon")
        }

    }


    class CollectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var frameLayout: FrameLayout = itemView.findViewById(R.id.collection_item)
        var image_preview: ImageView = itemView.findViewById(R.id.collection_image)
        var author_image: ImageView = itemView.findViewById(R.id.photographer_img)
        var collection_name: TextView = itemView.findViewById(R.id.item_collection_name)
        var collection_count: TextView = itemView.findViewById(R.id.item_collection_size)
    }

    fun setImageInfo(photosList: List<PhotoCollection>) {
        this.collectionPhotoList = photosList
        notifyDataSetChanged()
    }

    fun addImageInfo(photosList: List<PhotoCollection>) {
        if (this.collectionPhotoList is ArrayList) {
            (this.collectionPhotoList as ArrayList<PhotoCollection>).addAll(photosList)
        }
        notifyDataSetChanged()
    }

}