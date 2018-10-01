/*
 * DayTime:9/13/18 12:04 PM :
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
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.activities.PhotoDetails
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.image_utills.ColorShifter
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.google.gson.Gson
import java.util.*


class WallPackPhotoAdapter(private var wallPackList: List<Photos>, context: Context) :
        RecyclerView.Adapter<WallPackPhotoAdapter.WallPackHolder>() {

    private val TAG = "WallPackPhotoAdapter"
    var context: Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallPackHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.photos_layout, null)
        return WallPackHolder(itemView)
    }

    override fun getItemCount(): Int {
        return wallPackList.size
    }

    override fun onBindViewHolder(holder: WallPackHolder, position: Int) {

        val wallPackPhotos = wallPackList[position]
        val fadeAnimation = ViewPropertyTransition.Animator { view ->
            val fadeAnimation = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
            fadeAnimation.duration = 500// 500 is good for the eye
            fadeAnimation.start()
        }

        holder.cardView.setBackgroundColor(ColorShifter.computeCardBackgroundColor(context, wallPackPhotos.color))
        //Author image
        Glide.with(context)
                .load(wallPackPhotos.user.profile_image.medium)
                .into(holder.authorImage)
        //DisplayMetrics
        val displayMetrics = context.resources.displayMetrics
        val imageHeight: Float = displayMetrics.widthPixels / (wallPackPhotos.width.toFloat() / wallPackPhotos.height.toFloat())


        Glide.with(context)
                .load(wallPackPhotos.urls.regular)
                .transition(GenericTransitionOptions.with(fadeAnimation))
                .into(holder.imagePreview)
        holder.imagePreview.minimumHeight = imageHeight.toInt()

        holder.imagePreview.setOnClickListener {
            val gSon = Gson().toJson(wallPackPhotos)
            val i = Intent(context, PhotoDetails::class.java)
            i.putExtra("Photo", Gson().toJson(wallPackPhotos))
            context.startActivity(i)
            Log.d(TAG, "ImageClicked$gSon")
        }

    }

    class WallPackHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: FrameLayout = itemView.findViewById(R.id.cardview)
        var imagePreview: ImageView = itemView.findViewById(R.id.image_preview)
        var authorImage: ImageView = itemView.findViewById(R.id.author_image)
    }

    fun setImageInfo(photosList: List<Photos>) {
        this.wallPackList = photosList
        notifyDataSetChanged()
    }

    fun addImageInfo(photosList: List<Photos>) {
        if (this.wallPackList is ArrayList) {
            (this.wallPackList as ArrayList<Photos>).addAll(photosList)
        }
        notifyDataSetChanged()
    }
}

