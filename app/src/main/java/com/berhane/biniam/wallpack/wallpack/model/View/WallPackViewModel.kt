/*
 * DayTime:9/6/18 4:03 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.View

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.berhane.biniam.wallpack.wallpack.model.data.PhotoCollection
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.model.data.User
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.connectivity.RetrofitClient


class WallPackViewModel : ViewModel() {

    private var wallPackLiveData: LiveData<List<Photos>>? = null
    private var wallPackPhotoCollection: LiveData<List<PhotoCollection>>? = null
    private var retrofitClient: RetrofitClient? = null

    /**
     * List of new Photos from UnSplash.com
     */
    fun getPhotosList(pageIndex: Int, categoryId: Int): LiveData<List<Photos>>? {
        if (null == retrofitClient) {
            retrofitClient = RetrofitClient.getRetrofitClient()
        }
        if (-1 != categoryId) {
            this.wallPackLiveData = retrofitClient!!.requestPhotosByCategory(categoryId, pageIndex, PhotoConstants.PERPAGE)
        } else {
            this.wallPackLiveData = retrofitClient!!.requestImages(pageIndex, PhotoConstants.PERPAGE, PhotoConstants.TRENDING)
        }
        return wallPackLiveData!!
    }

    /**
     * get All the collection
     */
    fun getPhotoCollection(page: Int, perPage: Int): LiveData<List<PhotoCollection>>? {
        if (null == retrofitClient) {
            retrofitClient = RetrofitClient.getRetrofitClient()
        }
        this.wallPackPhotoCollection = retrofitClient!!.requestPhotoCollections(page, perPage)
        return wallPackPhotoCollection!!

    }

    /**
     * collection of Photos organized by Collection Id
     */
    fun getPhotoCollectionById(collections: PhotoCollection, page: Int, perPage: Int): LiveData<List<Photos>>? {
        if (null == retrofitClient) {
            retrofitClient = RetrofitClient.getRetrofitClient()
        }
        this.wallPackLiveData = retrofitClient!!.requestPhotosCollectionsById(collections, page, perPage)
        return wallPackLiveData!!
    }

    /**
     * Will show the Photographers Photos and Collection with Likes by Sort Order
     */
    fun getPhotographersPhotos(photographer: User, page: Int, perPage: Int, sortOrder: String): LiveData<List<Photos>>? {
        if (null == retrofitClient) {
            retrofitClient = RetrofitClient.getRetrofitClient()
        }
        this.wallPackLiveData = retrofitClient!!.requestPhotographerPhotos(photographer, page, perPage, sortOrder)
        return wallPackLiveData!!
    }

    /**
     * fetch the photographer Likes
     */
    fun getPhotographerLikes(photographer: User, page: Int, perPage: Int, sortOrder: String): LiveData<List<Photos>>? {
        if (null == retrofitClient) {
            retrofitClient = RetrofitClient.getRetrofitClient()
        }
        this.wallPackLiveData = retrofitClient!!.requestPhotographerLikes(photographer, page, perPage, sortOrder)
        return wallPackLiveData!!
    }

    /**
     * Photographer Collection will be  collected from this end
     */
    fun getPhotographerCollection(photographer: User, page: Int, perPage: Int): LiveData<List<PhotoCollection>>? {
        if (null == retrofitClient) {
            retrofitClient = RetrofitClient.getRetrofitClient()
        }
        this.wallPackPhotoCollection = retrofitClient!!.requestPhotographerCollection(photographer, page, perPage)
        return wallPackPhotoCollection!!
    }

    /**
     * Featured collection of Photos
     */
    fun getFeaturedCollection(page: Int, perPage: Int): LiveData<List<PhotoCollection>>? {
        if (null == retrofitClient) {
            retrofitClient = RetrofitClient.getRetrofitClient()
        }
        this.wallPackPhotoCollection = retrofitClient!!.requestFeaturedPhotoCollections(page, perPage)
        return wallPackPhotoCollection!!

    }

    /**
     * curated collection
     */
    fun getCuratedCollection(page: Int, perPage: Int): LiveData<List<PhotoCollection>>? {
        if (null == retrofitClient) {
            retrofitClient = RetrofitClient.getRetrofitClient()
        }
        this.wallPackPhotoCollection = retrofitClient!!.requestCuratedPhotoCollections(page, perPage)
        return wallPackPhotoCollection!!

    }

    /**
     * LiveData Curated Photos
     */
    fun getCuratedPhotos(page: Int, perPage: Int, sortOrder: String): LiveData<List<Photos>>? {
        if (null == retrofitClient) {
            retrofitClient = RetrofitClient.getRetrofitClient()
        }
        this.wallPackLiveData = retrofitClient!!.requestCuratedPhotos(page, perPage, sortOrder)
        return wallPackLiveData!!
    }
}