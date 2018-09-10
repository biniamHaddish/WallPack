/*
 * DayTime:9/7/18 10:46 AM :
 * Year:2018 :
 * Author:bini :
 */

/*
 * DayTime:9/5/18 2:38 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data
data class Photos(var id: String,
                  var created_at: String,
                  var updated_at: String,
                  var width: Int,
                  var height: Int,
                  var color: String,
                  var likes: Int,
                  var liked_by_user: Boolean,
                  var description: String,
                  var user: User,
                  var current_user_collections: List<PhotoCollection>,
                  var urls: PhotoUrls,
                  var categories: List<PhotoCategory>,
                  var links: PhotoDownloadLinks
)