/*
 * DayTime:10/1/18 4:14 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi

import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.utils.image_utills.DisplayUtils

class StatusBarView : View {

    private var alphaAnimator: ObjectAnimator? = null

    // if set true, the status bar view will add a translucent black drawMask when the view is being drawn.
    private var drawMask = false

    private var translucentMode = false // set the view is translucent or not.
    private var fillInMode = false // set the view is just used to fill in a blank area.

    var isInitState = false

    // these 2 values only effective in translucent mode. for example:
    // initAlpha < 0  --> initAlpha = LIGHT_INIT_MASK_ALPHA / DARK_INIT_MASK_ALPHA.
    // initAlpha >= 0 --> initAlpha = custom value.
    @FloatRange(to = 1.0)
    private var initAlpha = -1f
    @FloatRange(to = 1.0)
    private var darkerAlpha = -1f

    /**
     * Get the alpha by build version code, theme and state.
     */
    private val targetAlpha: Float
        get() = if (isInitState) {
            if (initAlpha >= 0) {
                initAlpha
            } else if (ThemeManager.getInstance(context).isLightTheme) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    LIGHT_INIT_MASK_ALPHA
                } else {
                    DARKER_MASK_ALPHA
                }
            } else {
                DARK_INIT_MASK_ALPHA
            }
        } else {
            if (darkerAlpha >= 0) darkerAlpha else DARKER_MASK_ALPHA
        }

    constructor(context: Context) : super(context) {
        this.initialize(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.initialize(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.initialize(context, attrs, defStyleAttr, 0)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.initialize(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initialize(c: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val a = c.obtainStyledAttributes(attrs, R.styleable.StatusBarView, defStyleAttr, defStyleRes)
        this.translucentMode = a.getBoolean(R.styleable.StatusBarView_sbv_translucent_mode, false)
        this.fillInMode = a.getBoolean(R.styleable.StatusBarView_sbv_fill_in_mode, false)
        this.initAlpha = Math.min(1.0f, a.getFloat(R.styleable.StatusBarView_sbv_init_alpha, -1f))
        this.darkerAlpha = Math.min(1.0f, a.getFloat(R.styleable.StatusBarView_sbv_darker_alpha, -1f))
        a.recycle()

        if (translucentMode) {
            setBackgroundResource(android.R.color.black)
            setInitTranslucentAlpha()
        } else if (fillInMode) {
            setBackgroundColor(Color.TRANSPARENT)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && ThemeManager.getInstance(c).isLightTheme) {
            setDrawMask(true)
            setBackgroundColor(ThemeManager.getPrimaryColor(context))
        } else {
            setDrawMask(false)
            setBackgroundColor(ThemeManager.getPrimaryDarkColor(context))
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
                View.MeasureSpec.getSize(widthMeasureSpec),
                DisplayUtils.getStatusBarHeight(resources))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (drawMask && !fillInMode) {
            canvas.drawColor(Color.argb((255 * 0.1).toInt(), 0, 0, 0))
        }
    }

    /**
     * Set draw drawMask or not.
     *
     * @param draw draw drawMask or not.
     */
    fun setDrawMask(draw: Boolean) {
        this.drawMask = draw
        invalidate()
    }

    /**
     * Reset the alpha of StatusBarView to the initially state.
     */
    fun setInitTranslucentAlpha() {
        if (translucentMode) {
            isInitState = true
            cancelAnimator()
            alpha = targetAlpha
        }
    }

    /**
     * Start alpha animation to change the alpha to initially state.
     */
    fun animToInitAlpha() {
        if (translucentMode) {
            isInitState = true
            changeAlpha(targetAlpha)
        }
    }

    /**
     * Start alpha animation to change the alpha to darker state.
     */
    fun animToDarkerAlpha() {
        if (translucentMode) {
            isInitState = false
            changeAlpha(targetAlpha)
        }
    }

    private fun changeAlpha(alphaTo: Float) {
        cancelAnimator()
        if (alpha != alphaTo) {
            alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", alpha, alphaTo)
                    .setDuration(150)
            alphaAnimator!!.interpolator = AccelerateDecelerateInterpolator()
            alphaAnimator!!.start()
        }
    }

    private fun cancelAnimator() {
        if (alphaAnimator != null) {
            alphaAnimator!!.cancel()
        }
    }

    fun setDarkerAlpha(alpha: Float) {
        darkerAlpha = alpha
    }

    companion object {

        val LIGHT_INIT_MASK_ALPHA = 0.03f
        val DARK_INIT_MASK_ALPHA = 0.2f
        val DARKER_MASK_ALPHA = 0.2f
    }
}
