/*
 * DayTime:10/19/18 10:07 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.View.frag.SearchPagerAdapter
import kotlinx.android.synthetic.main.activity_search__photos.*

class SearchPhotos : AppCompatActivity(), TextView.OnEditorActionListener {

    private lateinit var mContext: Context
    private var mClearTextAction: MenuItem? = null
    private var viewPager: ViewPager? = null
    private var tabsCollection: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search__photos)
        val upArrow = resources.getDrawable(R.drawable.abc_ic_ab_back_material, theme)
        upArrow.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeAsUpIndicator(upArrow)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mContext = this
        search_editText!!.setOnEditorActionListener(this)
        search_editText.isFocusable = true
        search_editText.requestFocus()

        search_editText.addTextChangedListener(textChangedWatcher)
        if (search_editText.requestFocus() && search_editText.text.toString() == "") {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
        viewPager = findViewById(R.id.search_viewpager)
        tabsCollection = findViewById(R.id.searchView_tabs)
        tabsCollection!!.setupWithViewPager(viewPager)

    }

    public override fun onResume() {
        super.onResume()
        if (search_editText.text.toString() != "") {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        mClearTextAction = menu!!.getItem(0)
        if (search_editText.text.toString() != "") mClearTextAction!!.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_clear_text -> {
                search_editText.text = null
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onEditorAction(textView: TextView?, i: Int, keyEvent: KeyEvent?): Boolean {
        val text = textView!!.text.toString()
        if (text != "") {
            // Try to implement the fragment adapter here
            //pager
            val fragmentAdapter = SearchPagerAdapter(supportFragmentManager, text)
            viewPager!!.adapter = fragmentAdapter

            viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    viewPager!!.currentItem = position
                }
            })
        }
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        return true
    }

    // Watching the text Changes and act on them
    private val textChangedWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            if (mClearTextAction != null) {
                mClearTextAction!!.isVisible = search_editText!!.text.toString() != ""
            }

        }
    }

}
