/*
 * DayTime:9/5/18 2:39 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils.connectivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.berhane.biniam.wallpack.wallpack.api.Authorization
import com.berhane.biniam.wallpack.wallpack.api.UnSplashApi
import com.berhane.biniam.wallpack.wallpack.model.data.*
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.TLSUtil
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitClient : TLSUtil() {


    val TAG = "RetrofitClient"
    private var call: Call<*>? = null

    //To make a reference for the retrofit Class to other Classes
    companion object {
        fun getRetrofitClient(): RetrofitClient {
            return RetrofitClient()
        }
    }

    var gson = GsonBuilder().setLenient().create()!!
    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)!!
    /**
     * OkHttp Client instance
     */
    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(WallPackInterceptor())
                .build()
    }


    private fun buildClient(): OkHttpClient {
        return getClientBuilder()
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(AuthenticationInterceptor())
                .build()
    }

    /**
     * retrofit Client baseline
     */
    private fun retrofitClient(client: OkHttpClient): UnSplashApi {
        return Retrofit.Builder()
                .baseUrl(PhotoConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat
                (PhotoConstants.DATE_FORMAT).create()))
                .build()
                .create(UnSplashApi::class.java)
    }

    /**
     * Authorization Retrofit Client
     */
    private fun authorizationRetrofitClient(client: OkHttpClient): Authorization {
        return Retrofit.Builder()
                .baseUrl(PhotoConstants.UnSplash_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(Authorization::class.java)
    }

    /**
     * will request some images from Unsplash.com
     */
    fun requestImages(page: Int, perPage: Int, orderBy: String): LiveData<List<Photos>> {
        val getPhotos = retrofitClient(okHttpClient()).getPhotos(page, perPage, orderBy)
        val data: MutableLiveData<List<Photos>> = MutableLiveData()
        getPhotos.enqueue(object : retrofit2.Callback<List<Photos>> {
            override fun onResponse(call: Call<List<Photos>>, response: retrofit2.Response<List<Photos>>) {
                Log.d("Responde_code\t", response.code().toString())
                data.value = response.body()
            }

            override fun onFailure(call: Call<List<Photos>>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e("Retrofit", it) }
            }
        })
        return data
    }

    /**
     * requesting photo by given category
     */
    fun requestPhotosByCategory(id: Int, page: Int, per_page: Int): LiveData<List<Photos>> {
        val getPhotosInAGivenCategory = retrofitClient(okHttpClient()).getPhotosInAGivenCategory(id, page, per_page)
        val data: MutableLiveData<List<Photos>> = MutableLiveData()
        getPhotosInAGivenCategory.enqueue(object : Callback<List<Photos>> {
            override fun onResponse(call: Call<List<Photos>>, response: retrofit2.Response<List<Photos>>) {
                data.value = response.body()

            }

            override fun onFailure(call: Call<List<Photos>>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e("Retrofit", it) }
            }
        })
        return data
    }

    /**
     * Returning the collection of Photos Collected by UnSplash and Photographers around the world
     */
    fun requestPhotoCollections(page: Int, perPage: Int): LiveData<List<PhotoCollection>> {
        val getPhotoCollections = retrofitClient(okHttpClient()).getAllCollections(page, perPage)
        val data: MutableLiveData<List<PhotoCollection>> = MutableLiveData()
        getPhotoCollections.enqueue(object : Callback<List<PhotoCollection>> {
            override fun onResponse(call: Call<List<PhotoCollection>>, response: retrofit2.Response<List<PhotoCollection>>) {
                data.value = response.body()
                Log.d(TAG, "" + response.body().toString())
            }

            override fun onFailure(call: Call<List<PhotoCollection>>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e(TAG, it) }
            }
        })
        return data
    }

    /**
     * getting the photos by the Collection id
     */
    fun requestPhotosCollectionsById(collection: PhotoCollection, page: Int, perPage: Int): LiveData<List<Photos>> {
        val getPhotoCollectionsDetails = retrofitClient(okHttpClient()).getCollectionPhotos(collection.id, page, perPage)
        val data: MutableLiveData<List<Photos>> = MutableLiveData()
        getPhotoCollectionsDetails.enqueue(object : Callback<List<Photos>> {
            override fun onResponse(call: Call<List<Photos>>, response: retrofit2.Response<List<Photos>>) {
                Log.d(TAG, "collection_by_ID" + response.body().toString())
                data.value = response.body()

            }

            override fun onFailure(call: Call<List<Photos>>, t: Throwable) {

                t.printStackTrace()
                Log.e(TAG, "fetching Error" + t.message)
            }
        })
        return data
    }

    /**
     * bring every collection from UnSplash.com
     */
    fun requestFeaturedPhotoCollections(page: Int, perPage: Int): LiveData<List<PhotoCollection>> {
        val getPhotoCollections = retrofitClient(okHttpClient()).getFeaturedCollections(page, perPage)
        val data: MutableLiveData<List<PhotoCollection>> = MutableLiveData()
        getPhotoCollections.enqueue(object : Callback<List<PhotoCollection>> {
            override fun onResponse(call: Call<List<PhotoCollection>>, response: retrofit2.Response<List<PhotoCollection>>) {
                data.value = response.body()
                Log.d(TAG, "FeaturedPhotoCollections\t" + response.body().toString())
            }

            override fun onFailure(call: Call<List<PhotoCollection>>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e(TAG, it) }
            }
        })
        return data
    }

    /**
     * Requesting the particular photographer Photos
     */
    fun requestPhotographerPhotos(photographer: User, page: Int, PerPage: Int, sortOrder: String): LiveData<List<Photos>> {
        val PhotographerPhotos = retrofitClient(okHttpClient()).getPhotographerPhotos(photographer.username, page, PerPage, sortOrder)
        val data: MutableLiveData<List<Photos>> = MutableLiveData()
        PhotographerPhotos.enqueue(object : Callback<List<Photos>> {
            override fun onFailure(call: Call<List<Photos>>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e(TAG, it) }
            }

            override fun onResponse(call: Call<List<Photos>>, response: Response<List<Photos>>) {
                data.value = response.body()
                Log.d(TAG, "thePhotographerCallCode\t" + response.code().toString())
                Log.d(TAG, "requestPhotographerPhotos\t" + response.body()!!.iterator().forEach {
                    Log.d(TAG, "listOf" + it.urls.regular)
                })
            }

        })

        return data
    }


    /**
     * Requesting photographer Likes
     */
    fun requestPhotographerLikes(photographer: User, page: Int, PerPage: Int, sortOrder: String): LiveData<List<Photos>> {
        val getPhotographerLikes = retrofitClient(okHttpClient()).getPhotographerLikes(photographer.username, page, PerPage, sortOrder)
        val data: MutableLiveData<List<Photos>> = MutableLiveData()
        getPhotographerLikes.enqueue(object : Callback<List<Photos>> {
            override fun onFailure(call: Call<List<Photos>>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e(TAG, it) }
            }

            override fun onResponse(call: Call<List<Photos>>, response: Response<List<Photos>>) {
                data.value = response.body()
                Log.d(TAG, "the Photographer Call Code\t" + response.code().toString())
            }

        })
        return data
    }

    /**
     * Requesting Photographer Collections
     */
    fun requestPhotographerCollection(photographer: User, page: Int, PerPage: Int): LiveData<List<PhotoCollection>> {
        val getPhotographerCollection = retrofitClient(okHttpClient()).getPhotographerCollections(photographer.username, page, PerPage)
        val data: MutableLiveData<List<PhotoCollection>> = MutableLiveData()
        getPhotographerCollection.enqueue(object : Callback<List<PhotoCollection>> {
            override fun onFailure(call: Call<List<PhotoCollection>>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e(TAG, it) }
            }

            override fun onResponse(call: Call<List<PhotoCollection>>, response: Response<List<PhotoCollection>>) {
                data.value = response.body()
                Log.d(TAG, "the Photographer Call Code\t" + response.code().toString())
            }

        })
        return data
    }

    /**
     * Requesting  every Curated Collection Of Photos
     */
    fun requestCuratedPhotoCollections(page: Int, perPage: Int): LiveData<List<PhotoCollection>> {
        val getPhotoCollections = retrofitClient(okHttpClient()).getCuratedCollections(page, perPage)
        val data: MutableLiveData<List<PhotoCollection>> = MutableLiveData()
        getPhotoCollections.enqueue(object : Callback<List<PhotoCollection>> {
            override fun onResponse(call: Call<List<PhotoCollection>>, response: retrofit2.Response<List<PhotoCollection>>) {
                data.value = response.body()
                Log.e(TAG, "" + response.body().toString())
            }

            override fun onFailure(call: Call<List<PhotoCollection>>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e(TAG, it) }
            }
        })
        return data
    }


    /**
     *Will request photo my name from unsplash.com
     */
    fun requestSearchedPhoto(query: String, page: Int, PerPage: Int): LiveData<List<Photos>> {
        val getSearchedPhotos = retrofitClient(okHttpClient()).searchPhotos(query, page, PerPage)
        val data: MutableLiveData<List<Photos>> = MutableLiveData()
        getSearchedPhotos.enqueue(object : Callback<SearchResult> {
            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e(TAG, it) }
            }

            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                if (response.body()!!.results != null) {
                    data.value = response.body()?.results
                    Log.d(TAG, "SearchResult\t" + response.body().toString())
                }
            }

        })
        return data
    }

    /**
     * request Photographer Data from UnSplash.com
     */
    fun requestPhotographerSearchResult(query: String, page: Int, PerPage: Int): LiveData<List<User>> {
        val getSearchedPhotos = retrofitClient(okHttpClient()).searchPhotographers(query, page, PerPage)
        val data: MutableLiveData<List<User>> = MutableLiveData()
        getSearchedPhotos.enqueue(object : Callback<SearchUserResult> {
            override fun onFailure(call: Call<SearchUserResult>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e(TAG, it) }
            }

            override fun onResponse(call: Call<SearchUserResult>, response: Response<SearchUserResult>) {
                if (response.body()!!.results != null) {
                    data.value = response.body()?.results
                }
            }

        })
        return data
    }

    fun requestCollectionSearchResult(query: String, page: Int, PerPage: Int): LiveData<List<PhotoCollection>> {
        val getPhotoCollections = retrofitClient(okHttpClient()).searchCollections(query, page, PerPage)
        val data: MutableLiveData<List<PhotoCollection>> = MutableLiveData()
        getPhotoCollections.enqueue(object : Callback<CollectionSerachResult> {
            override fun onResponse(call: Call<CollectionSerachResult>, response: retrofit2.Response<CollectionSerachResult>) {
                if (response.body()!!.results != null) {
                    data.value = response.body()!!.results
                    Log.e(TAG, "CollectionSearchResult" + response.body().toString())
                }

            }

            override fun onFailure(call: Call<CollectionSerachResult>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e(TAG, it) }
            }
        })
        return data
    }

    /**
     * Will return a Curated Photos depending on the Sort Order
     */
    fun requestCuratedPhotos(page: Int, perpage: Int, sortOrder: String): LiveData<List<Photos>> {
        val getCuratedPhotos = retrofitClient(okHttpClient()).getCuratedPhotos(page, perpage, sortOrder)
        val data: MutableLiveData<List<Photos>> = MutableLiveData()
        getCuratedPhotos.enqueue(object : Callback<List<Photos>> {
            override fun onFailure(call: Call<List<Photos>>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e(TAG, it) }
            }

            override fun onResponse(call: Call<List<Photos>>, response: Response<List<Photos>>) {
                Log.d(TAG, response.body().toString())
                data.value = response.body()
            }

        })
        return data
    }


    fun requestAccessToken(code: String, l: OnRequestAccessTokenListener?) {

        val getAccessToken = authorizationRetrofitClient(buildClient()).getAccessToken(
                PhotoConstants.ACCESS_KEY,
                PhotoConstants.SECRET_KEY,
                PhotoConstants.REDIRECT_URI,
                code,
                PhotoConstants.GRANTE_TYPE)

        getAccessToken.enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                l?.onRequestAccessTokenSuccess(call, response)
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                l?.onRequestAccessTokenFailed(call, t)
            }
        })
        call = getAccessToken
    }

    fun requestMeProfile(l: OnRequestMeProfileListener?) {
        val getMeProfile = retrofitClient(buildClient()).getMeProfile()
        getMeProfile.enqueue(object : Callback<Me> {
            override fun onResponse(call: Call<Me>, response: Response<Me>) {
                l?.onRequestMeProfileSuccess(call, response)
            }

            override fun onFailure(call: Call<Me>, t: Throwable) {
                l?.onRequestMeProfileFailed(call, t)
            }
        })
        call = getMeProfile
    }

    fun requestUserProfile(username: String, l: OnRequestUserProfileListener?) {
        val getUserProfile = retrofitClient(buildClient()).getUserProfile(username, 256, 256)
        getUserProfile.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                l?.onRequestUserProfileSuccess(call, response)
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                l?.onRequestUserProfileFailed(call, t)
            }
        })
        call = getUserProfile
    }

    fun reportPhotoDownload(id: String, l: OnReportDownloadListener?) {
        val reportDownload = retrofitClient(buildClient()).reportPhotoDownload(id)
        reportDownload.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (l != null) {
                    l!!.onReportDownloadSuccess(call, response)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (l != null) {
                    l!!.onReportDownloadFailed(call, t)
                }
            }
        })
    }

    fun setLikeForAPhoto(id: String, like: Boolean, l: OnSetLikeListener?) {
        val setLikeForAPhoto = if (like) retrofitClient(buildClient()).likeAPhoto(id)
        else
            retrofitClient(buildClient()).unlikeAPhoto(id)
        setLikeForAPhoto.enqueue(object : Callback<PhotoLike> {
            override fun onResponse(call: Call<PhotoLike>, response: Response<PhotoLike>) {
                if (l != null) {
                    l!!.onSetLikeSuccess(call, response)
                }
            }

            override fun onFailure(call: Call<PhotoLike>, t: Throwable) {
                if (l != null) {
                    l!!.onSetLikeFailed(call, t)
                }
            }
        })
    }

    fun requestPhotoDetails(id: String, l: OnRequestPhotoDetailsListener?) {
        val getAPhoto = retrofitClient(buildClient()).getSinglePhoto(id)
        getAPhoto.enqueue(object : Callback<PhotoDetails> {
            override fun onResponse(call: Call<PhotoDetails>, response: Response<PhotoDetails>) {
                if (l != null) {
                    l!!.onRequestPhotoDetailsSuccess(call, response)
                }
            }

            override fun onFailure(call: Call<PhotoDetails>, t: Throwable) {
                if (l != null) {
                    l!!.onRequestPhotoDetailsFailed(call, t)
                }
            }
        })
    }

    /**
     * Cancel the call for the end point
     */
    fun cancelCall() {
        if (call != null) {
            call!!.cancel()
        }
    }

    // interface.
    interface OnRequestPhotoDetailsListener {
        fun onRequestPhotoDetailsSuccess(call: Call<PhotoDetails>, response: Response<PhotoDetails>)

        fun onRequestPhotoDetailsFailed(call: Call<PhotoDetails>, t: Throwable)
    }

    interface OnRequestUserProfileListener {
        fun onRequestUserProfileSuccess(call: Call<User>, response: Response<User>)
        fun onRequestUserProfileFailed(call: Call<User>, t: Throwable)
    }

    interface OnRequestMeProfileListener {
        fun onRequestMeProfileSuccess(call: Call<Me>, response: Response<Me>)
        fun onRequestMeProfileFailed(call: Call<Me>, t: Throwable)
    }

    interface OnRequestUsersListener {
        fun onRequestUsersSuccess(call: Call<List<User>>, response: Response<List<User>>)
        fun onRequestUsersFailed(call: Call<List<User>>, t: Throwable)
    }

    interface OnSetLikeListener {
        fun onSetLikeSuccess(call: Call<PhotoLike>, response: Response<PhotoLike>)

        fun onSetLikeFailed(call: Call<PhotoLike>, t: Throwable)
    }

    interface OnReportDownloadListener {
        fun onReportDownloadSuccess(call: Call<ResponseBody>, response: Response<ResponseBody>)

        fun onReportDownloadFailed(call: Call<ResponseBody>, t: Throwable)
    }

    interface OnRequestAccessTokenListener {
        fun onRequestAccessTokenSuccess(call: Call<AccessToken>, response: retrofit2.Response<AccessToken>)

        fun onRequestAccessTokenFailed(call: Call<AccessToken>, t: Throwable)
    }
}



