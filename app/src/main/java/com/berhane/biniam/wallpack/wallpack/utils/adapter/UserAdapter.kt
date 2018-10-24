/*
 * DayTime:10/24/18 4:17 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.model.data.User
import com.bumptech.glide.Glide
import java.util.*


class UserAdapter(userList: MutableList<User>, context: Context) :
        RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var context: Context = context
    var userListCollect: List<User> = userList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.photographer_layout, null)
        return UserViewHolder(itemView)
    }

    override fun getItemCount() = userListCollect.size

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val userData = userListCollect[position]
        //Bio image of the photographer
        Glide.with(context)
                .load(userData.profile_image.large)
                .into(holder.photographerImg)
        //setting the name of photographer
        holder.photographerName.text = userData.name
        // Setting the Photographer Description
        holder.photographerDescription.text = userData.bio

    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var parentViewContainer: FrameLayout = itemView.findViewById(R.id.container_main)
        var photographerName: TextView = itemView.findViewById(R.id.photographer_name_ly)
        var photographerImg: ImageView = itemView.findViewById(R.id.photographer_img_cir)
        var photographerDescription: TextView = itemView.findViewById(R.id.photographer_description)
    }


    /**
     * Setting the user to the adapter
     */
    fun setUser(userList: MutableList<User>) {
        this.userListCollect = userList
        notifyDataSetChanged()
    }

    /**
     * Adding the User to the Adapter
     */
    fun addAllUser(userList: List<User>) {
        if (this.userListCollect is ArrayList) {
            (this.userListCollect as ArrayList<User>).addAll(userList)
        }
        notifyDataSetChanged()
    }
}