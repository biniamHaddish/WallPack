/*
 * DayTime:9/5/18 2:39 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils.connectivity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.berhane.biniam.wallpack.wallpack.api.UnsplashApi
import com.berhane.biniam.wallpack.wallpack.model.data.PhotoCollection
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
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
     * OkHttp Client
     */
    private fun okhttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
                .addInterceptor(WallPackInterceptor())
                .build()
    }

    /**
     *
     */
    private fun retrofitClient(client: OkHttpClient): UnsplashApi {
        return Retrofit.Builder()
                .baseUrl(PhotoConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat
                (PhotoConstants.DATE_FORMAT).create()))
                .build()
                .create(UnsplashApi::class.java)
    }

    /**
     * will request some images from Unsplash.com
     */
    fun requestImages(page: Int, perPage: Int, orderBy: String): LiveData<List<Photos>> {
        val getPhotos = retrofitClient(okhttpClient()).getPhotos(page, perPage, orderBy)
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
     *
     */
    fun requestPhotosByCategory(id: Int, page: Int, per_page: Int): LiveData<List<Photos>> {
        val getPhotosInAGivenCategory = retrofitClient(okhttpClient()).getPhotosInAGivenCategory(id, page, per_page)
        val data: MutableLiveData<List<Photos>> = MutableLiveData()
        getPhotosInAGivenCategory.enqueue(object : Callback<List<Photos>> {
            override fun onResponse(call: Call<List<Photos>>, response: retrofit2.Response<List<Photos>>) {
                data.value = response.body()

            }

            override fun onFailure(call: Call<List<Photos>>, t: Throwable) {
                t.printStackTrace()
                Log.d("Retrofit", t.message)
            }
        })
        return data
    }

    /**
     * Returning the collection of Photos
     */
    fun requestPhotoCollections(page: Int, perPage: Int): LiveData<List<PhotoCollection>> {
        val getPhotoCollections = retrofitClient(okhttpClient()).getAllCollections(page, perPage)
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
}

