/*
 * DayTime:9/13/18 11:43 AM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.model.View.WallPackViewModel
import com.berhane.biniam.wallpack.wallpack.model.data.PhotoCollection
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.FragmentArgumentDelegate
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.adapter.CollectionAdapter
import com.bumptech.glide.Glide
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView

class CollectionFragment : Fragment() {

    private var pageNumber: Int = 1
    private lateinit var viewModel: WallPackViewModel
    private lateinit var mRecyclerView: XRecyclerView
    private var viewAdapter: CollectionAdapter? = null

    val TAG = "CollectionFragment"

    private var collectionType by FragmentArgumentDelegate<String>()

    companion object {
        fun newInstance(collectionType: String) = CollectionFragment().apply {
            this.collectionType = collectionType
        }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        Log.d(TAG, "fragment Attatched")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.collection_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(WallPackViewModel::class.java)
        mRecyclerView = rootView.findViewById(R.id.CollectionRecyclerView)
        mRecyclerView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> false
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        return rootView
    }

    override fun onResume() {
        super.onResume()
        loadPhotoCollections(false)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        Glide.with(this@CollectionFragment).resumeRequests()
                    }
                    else -> {
                        Glide.with(this@CollectionFragment).pauseRequests()
                    }
                }
            }
        })

    }

    /**
     * Will let the fragment start after the base activity is finished
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initCollection()
    }

    /**
     * Will Load more CollectionFragment if the value is true
     */
    fun loadPhotoCollections(moreCollection: Boolean) {
        if (collectionType == PhotoConstants.COLLECTION_TYPE_ALL) {

            viewModel.getPhotoCollection(pageNumber, PhotoConstants.PERPAGE)!!.observe(this@CollectionFragment,
                    Observer<List<PhotoCollection>> { t: List<PhotoCollection>? ->
                        if (viewAdapter == null) {
                            viewAdapter = CollectionAdapter(t!!, activity as Activity)
                            mRecyclerView.adapter = viewAdapter
                        } else {
                            if (moreCollection) {
                                viewAdapter!!.addImageInfo(t!!)
                                mRecyclerView.loadMoreComplete()
                            } else {
                                viewAdapter!!.setImageInfo(t!!)
                                mRecyclerView.refreshComplete()
                            }
                        }

                    }
            )
        } else if (collectionType == (PhotoConstants.COLLECTION_TYPE_FEATURED)) {

            viewModel.getFeaturedCollection(pageNumber, PhotoConstants.PERPAGE)!!.observe(this@CollectionFragment,
                    Observer<List<PhotoCollection>> { t: List<PhotoCollection>? ->
                        if (viewAdapter == null) {
                            viewAdapter = CollectionAdapter(t!!, activity as Activity)
                            mRecyclerView.adapter = viewAdapter
                        } else {
                            if (moreCollection) {
                                viewAdapter!!.addImageInfo(t!!)
                                mRecyclerView.loadMoreComplete()
                            } else {
                                viewAdapter!!.setImageInfo(t!!)
                                mRecyclerView.refreshComplete()
                            }
                        }

                    }
            )

        } else if (collectionType == (PhotoConstants.COLLECTION_TYPE_CURATED)) {

            viewModel.getCuratedCollection(pageNumber, PhotoConstants.PERPAGE)!!.observe(this@CollectionFragment,
                    Observer<List<PhotoCollection>> { t: List<PhotoCollection>? ->
                        if (viewAdapter == null) {
                            viewAdapter = CollectionAdapter(t!!, activity as Activity)
                            mRecyclerView.adapter = viewAdapter
                        } else {
                            if (moreCollection) {
                                viewAdapter!!.addImageInfo(t!!)
                                mRecyclerView.loadMoreComplete()
                            } else {
                                viewAdapter!!.setImageInfo(t!!)
                                mRecyclerView.refreshComplete()
                            }
                        }

                    }
            )
        }

    }

    /**
     * initializing the value of our CollectionFragment here
     */
    fun initCollection() {
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse)
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulseSync)
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow)
        mRecyclerView.defaultFootView
        mRecyclerView.visibility = View.VISIBLE
        mRecyclerView.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                ++pageNumber
                loadPhotoCollections(true)
            }

            override fun onRefresh() {
                pageNumber = 1
                loadPhotoCollections(false)
            }
        })
        mRecyclerView.setOnClickListener(onClickListener)
    }

    private val onClickListener = object : View.OnClickListener {
        override fun onClick(p0: View?) {
            Log.d(TAG, p0.toString())
        }
//
//        fun onClick(v: View, adapter: CollectionAdapter, item: PhotoCollection, position: Int): Boolean {
//            val i = Intent(context, CollectionDetailActivity::class.java)
//            i.putExtra("Collection", Gson().toJson(item))
//            startActivity(i)
//            return false
//        }
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach")
        super.onDetach()
        viewAdapter = null
    }


    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
            mRecyclerView.destroy()
    }
}