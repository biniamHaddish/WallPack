/*
 * DayTime:9/5/18 2:39 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils.connectivity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.berhane.biniam.wallpack.wallpack.View.frag.SearchCollection
import com.berhane.biniam.wallpack.wallpack.api.UnSplashApi
import com.berhane.biniam.wallpack.wallpack.model.data.*
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient {


    val TAG = "RetrofitClient"

    //To make a reference for the retrofit Class to other Classes
    companion object {
        fun getRetrofitClient(): RetrofitClient {
            return RetrofitClient()
        }
    }

    /**
     * OkHttp Client instance
     */
    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
                .addInterceptor(WallPackInterceptor())
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
                Log.e("Retrofit", t.message)
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
                Log.e("Retrofit", t.message)
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
                Log.e(TAG, t.message)
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
                Log.e(TAG, t.message)
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
                Log.e(TAG, t.message)
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
                Log.e(TAG, t.message)
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
                Log.e(TAG, t.message)
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
                Log.e(TAG, t.message)
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
                Log.e(TAG, t.message)
            }

            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                Log.d(TAG, "SearchResult\t" + response.body().toString())
                data.value = response.body()?.results
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
                Log.e(TAG, t.message)
            }

            override fun onResponse(call: Call<SearchUserResult>, response: Response<SearchUserResult>) {
                Log.d(TAG, "Photographer_\t" + response.body().toString())
                data.value = response.body()?.results
            }

        })
        return data
    }

    fun requestCollectionSearchResult(query: String, page: Int, PerPage: Int): LiveData<List<PhotoCollection>> {
        val getPhotoCollections = retrofitClient(okHttpClient()).searchCollections(query, page, PerPage)
        val data: MutableLiveData<List<PhotoCollection>> = MutableLiveData()
        getPhotoCollections.enqueue(object : Callback<CollectionSerachResult> {
            override fun onResponse(call: Call<CollectionSerachResult>, response: retrofit2.Response<CollectionSerachResult>) {
                data.value = response.body()?.results
                Log.e(TAG, "CollectionSearchResult" + response.body().toString())
            }

            override fun onFailure(call: Call<CollectionSerachResult>, t: Throwable) {
                t.printStackTrace()
                Log.e(TAG, t.message)
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
                Log.e(TAG, t.message)
            }

            override fun onResponse(call: Call<List<Photos>>, response: Response<List<Photos>>) {
                Log.d(TAG, response.body().toString())
                data.value = response.body()
            }

        })
        return data
    }
}



