

/*
 * DayTime:9/7/18 10:46 AM :
 * Year:2018 :
 * Author:bini :
 */

/*
 * DayTime:9/5/18 2:39 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data

data class User(var id:String, var updated_at:String, var username:String, var name:String,
                var first_name:String, var last_name:String, var twitter_username:String,
                var portfolio_url:String, var bio:String, var location:String, var total_likes:Int,
                var total_photos:Int, var total_collections:Int, var
                profile_image: UserProfileImageUrl, var links: UserLinks)

