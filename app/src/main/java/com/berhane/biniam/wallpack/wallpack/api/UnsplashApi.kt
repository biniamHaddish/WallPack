package com.berhane.biniam.wallpack.wallpack.api

import com.berhane.biniam.wallpack.wallpack.model.data.PhotoCollection
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.http.*


interface UnsplashApi {

    @GET("photos")
    fun getPhotos(@Query("page") page: Int,
                  @Query("per_page") per_page: Int,
                  @Query("orderedBy") orderedBy: String): Call<List<Photos>>


    @GET("photos/curated")
    fun getCuratedPhotos(@Query("page") page: Int,
                         @Query("per_page") per_page: Int,
                         @Query("order_by") order_by: String): Call<List<Photos>>

//   @GET("photos/{id}/stats")
//    fun getPhotoStats(@Path("id") id: String): Call<PhotoStats>

    @GET("categories/{id}/photos")
    fun getPhotosInAGivenCategory(@Path("id") id: Int,
                                  @Query("page") page: Int,
                                  @Query("per_page") per_page: Int): Call<List<Photos>>

//    @POST("photos/{id}/like")
//    fun likeAPhoto(@Path("id") id: String): Call<LikePhotoResult>
//
//    @DELETE("photos/{id}/like")
//    fun unlikeAPhoto(@Path("id") id: String): Call<LikePhotoResult>
//
//    @GET("photos/{id}")
//    fun getAPhoto(@Path("id") id: String): Call<PhotoDetails>

    @GET("collections/{id}/photos")
     fun getCollectionPhotos(@Path("id") id: Int,
                                     @Query("page") page: Int,
                                     @Query("per_page") per_page: Int): Call<List<Photos>>

    @GET("collections")
    fun getAllCollections(@Query("page") page: Int,
                          @Query("per_page") per_page: Int): Call<List<PhotoCollection>>

    @GET("collections/curated")
    fun getCuratedCollections(@Query("page") page: Int,
                              @Query("per_page") per_page: Int): Call<List<PhotoCollection>>

    @GET("collections/curated/{id}/photos")
     fun getCuratedCollectionPhotos(@Path("id") id: Int,
                                            @Query("page") page: Int,
                                            @Query("per_page") per_page: Int): Call<List<Photos>>

    @GET("collections/featured")
    fun getFeaturedCollections(@Query("page") page: Int,
                               @Query("per_page") per_page: Int): Call<List<PhotoCollection>>

    @GET("users/{username}/collections")
    fun getUserCollections(@Path("username") username: String,
                           @Query("page") page: Int,
                           @Query("per_page") per_page: Int): Call<List<PhotoCollection>>

    @POST("collections")
    fun createCollection(@Query("title") title: String,
                         @Query("description") description: String,
                         @Query("private") privateX: Boolean): Call<PhotoCollection>

//    @GET("users/{username}/photos")
//    fun getUserPhotos(@Path("username") username: String,
//                      @Query("page") page: Int,
//                      @Query("per_page") per_page: Int,
//                      @Query("order_by") order_by: String): Call<List<Photos>>
//
//    @GET("users/{username}/likes")
//    fun getUserLikes(@Path("username") username: String,
//                     @Query("page") page: Int,
//                     @Query("per_page") per_page: Int,
//                     @Query("order_by") order_by: String): Call<List<Photos>>
//
//    @GET("collections/{id}/photos")
//    fun getCollectionPhotos(@Path("id") id: Int,
//                            @Query("page") page: Int,
//                            @Query("per_page") per_page: Int): Call<List<Photos>>
//
//    @GET("collections/curated/{id}/photos")
//    fun getCuratedCollectionPhotos(@Path("id") id: Int,
//                                   @Query("page") page: Int,
//                                   @Query("per_page") per_page: Int): Call<List<Photos>>

    @GET("photos/random")
    fun getRandomPhotos(@Query("category") categoryId: Int?,
                        @Query("featured") featured: Boolean?,
                        @Query("username") username: String,
                        @Query("query") query: String,
                        @Query("orientation") orientation: String,
                        @Query("count") count: Int): Call<List<Photos>>

    @GET("photos/{id}/download")
    fun reportDownload(@Path("id") id: String): Call<ResponseBody>

}