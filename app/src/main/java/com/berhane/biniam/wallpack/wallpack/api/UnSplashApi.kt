package com.berhane.biniam.wallpack.wallpack.api

import com.berhane.biniam.wallpack.wallpack.model.data.*
import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.http.*
import retrofit2.http.GET


interface UnSplashApi {

    @GET("photos")
    fun getPhotos(@Query("page") page: Int,
                  @Query("per_page") per_page: Int,
                  @Query("orderedBy") orderedBy: String): Call<List<Photos>>


    @GET("photos/curated")
    fun getCuratedPhotos(@Query("page") page: Int,
                         @Query("per_page") per_page: Int,
                         @Query("order_by") order_by: String): Call<List<Photos>>

   @GET("photos/{id}/stats")
   fun getPhotoStats(@Path("id") id: String): Call<Stats>

    @GET("categories/{id}/photos")
    fun getPhotosInAGivenCategory(@Path("id") id: Int,
                                  @Query("page") page: Int,
                                  @Query("per_page") per_page: Int): Call<List<Photos>>

    @POST("photos/{id}/like")
    fun likeAPhoto(@Path("id") id: String): Call<PhotoLike>

    @DELETE("photos/{id}/like")
    fun unlikeAPhoto(@Path("id") id: String): Call<PhotoLike>

    @GET("photos/{id}")
    fun getSinglePhoto(@Path("id") id: String): Call<PhotoDetails>

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
    fun getCuratedCollectionPhotosById(@Path("id") id: Int,
                                       @Query("page") page: Int,
                                       @Query("per_page") per_page: Int): Call<List<Photos>>

    @GET("collections/featured")
    fun getFeaturedCollections(@Query("page") page: Int,
                               @Query("per_page") per_page: Int): Call<List<PhotoCollection>>


    @POST("collections")
    fun createCollection(@Query("title") title: String,
                         @Query("description") description: String,
                         @Query("private") privateX: Boolean): Call<PhotoCollection>

    // user Collection, likes and photos
    @GET("users/{username}/photos")
    fun getPhotographerPhotos(@Path("username") username: String,
                              @Query("page") page: Int,
                              @Query("per_page") per_page: Int,
                              @Query("sortOrder") sortOrder: String): Call<List<Photos>>

    @GET("users/{username}/likes")
    fun getPhotographerLikes(@Path("username") username: String,
                             @Query("page") page: Int,
                             @Query("per_page") per_page: Int,
                             @Query("order_by") order_by: String): Call<List<Photos>>

    @GET("users/{username}/collections")
    fun getPhotographerCollections(@Path("username") username: String,
                                   @Query("page") page: Int,
                                   @Query("per_page") per_page: Int): Call<List<PhotoCollection>>

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
    fun reportPhotoDownload(@Path("id") id: String): Call<ResponseBody>


    // Search Api
    @GET("search/photos")
    fun searchPhotos(@Query("query") query: String,
                     @Query("page") page: Int,
                     @Query("perPage") perPage: Int): Call<SearchResult>

    @GET("search/users")
    fun searchPhotographers(@Query("query") query: String,
                            @Query("page") page: Int,
                            @Query("perPage") perPage: Int): Call<SearchUserResult>

    @GET("search/collections")
    fun searchCollections(@Query("query") query: String,
                          @Query("page") page: Int,
                          @Query("perPage") perPage: Int): Call<CollectionSerachResult>




    @GET("users/{username}")
    fun getUserProfile(@Path("username") username: String,
                       @Query("w") w: Int,
                       @Query("h") h: Int): Call<User>

    @GET("me")
    fun getMeProfile(): Call<Me>

    @PUT("me")
    fun updateMeProfile(@Query("username") username: String,
                        @Query("first_name") first_name: String,
                        @Query("last_name") last_name: String,
                        @Query("email") email: String,
                        @Query("url") url: String,
                        @Query("location") location: String,
                        @Query("bio") bio: String): Call<Me>

}