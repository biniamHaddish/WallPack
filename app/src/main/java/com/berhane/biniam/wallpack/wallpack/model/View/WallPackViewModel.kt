/*
 * DayTime:9/6/18 4:03 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.View

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.connectivity.RetrofitClient


class WallPackViewModel : ViewModel() {

    private var wallPackLiveData: LiveData<List<Photos>>? = null
    private var retrofitClient : RetrofitClient? = null

    //loading the Photos with LiveData
    fun  loadData(perpage :Int):LiveData<List<Photos>>?{
        if (retrofitClient==null) {
            retrofitClient= RetrofitClient.getRetrofitClient()
        }
        this.wallPackLiveData = retrofitClient!!.requestImages(perpage, PhotoConstants.perPage, PhotoConstants.latest)
        return wallPackLiveData
    }

    fun getPhotosList(pageIndex: Int, categoryId: Int): LiveData<List<Photos>>? {
        if (null == retrofitClient) {
            retrofitClient = RetrofitClient.getRetrofitClient()
        }
        if (-1!= categoryId) {
            this.wallPackLiveData = retrofitClient!!.requestPhotosByCategory(categoryId, pageIndex, PhotoConstants.perPage)
        } else {
            this.wallPackLiveData = retrofitClient!!.requestImages(pageIndex, PhotoConstants.perPage,
                    PhotoConstants.latest)
        }
        return wallPackLiveData!!
    }




}