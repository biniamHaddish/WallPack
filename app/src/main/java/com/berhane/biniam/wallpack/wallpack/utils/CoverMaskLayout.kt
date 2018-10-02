/*
 * DayTime:10/1/18 4:44 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.support.v4.graphics.ColorUtils
import android.util.AttributeSet
import android.widget.LinearLayout

import com.berhane.biniam.wallpack.wallpack.R

class CoverMaskLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private val paint: Paint

    private var gradientAngle: Float = 0.toFloat()
    private var fromAlpha: Float = 0.toFloat()
    private var toAlpha: Float = 0.toFloat()
    private var maskColor: Int = 0

    init {
        setWillNotDraw(false)
        paint = Paint()
        if (attrs !=
                null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CoverMaskLayout, defStyleAttr, 0)
            setGradientAngle(a.getFloat(R.styleable.CoverMaskLayout_cml_gradient_angle, 90f))
            fromAlpha = computeRealAlpha(a.getFloat(R.styleable.CoverMaskLayout_cml_from_alpha, 1f))
            toAlpha = computeRealAlpha(a.getFloat(R.styleable.CoverMaskLayout_cml_to_alpha, 0f))
            maskColor = a.getColor(R.styleable.CoverMaskLayout_cml_mask_color, Color.BLACK)
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setPaintStyle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
    }

    fun setGradientAngle(angle: Float) {
        gradientAngle = (angle + 360) % 360
        setPaintStyle()
        invalidate()
    }

    private fun computeRealAlpha(alpha: Float): Float {
        return if (alpha < 0) {
            0f
        } else if (alpha > 1) {
            1f
        } else {
            alpha
        }
    }

    private fun setPaintStyle() {
        var deltaX: Double
        var deltaY: Double
        if (gradientAngle == 90f || gradientAngle == 270f) {
            deltaX = 0.0
        } else {
            deltaX = 0.5 * measuredHeight / Math.tan(gradientAngle * Math.PI / 180.0)
            deltaX = Math.min(deltaX, measuredWidth * 0.5)
            deltaX = Math.max(deltaX, measuredWidth * -0.5)
        }
        if (gradientAngle == 90f) {
            deltaY = 0.5 * measuredHeight
        } else if (gradientAngle == 270f) {
            deltaY = -0.5 * measuredHeight
        } else {
            deltaY = 0.5 * measuredWidth.toDouble() * Math.tan(gradientAngle * Math.PI / 180.0)
            deltaY = Math.min(deltaY, measuredHeight * 0.5)
            deltaY = Math.max(deltaY, measuredHeight * -0.5)
        }

        val cX = measuredWidth * 0.5
        val cY = measuredHeight * 0.5
        paint.shader = LinearGradient(
                (cX + deltaX).toFloat(), (cY - deltaY).toFloat(),
                (cX - deltaX).toFloat(), (cY + deltaY).toFloat(),
                intArrayOf(ColorUtils.setAlphaComponent(maskColor, (255 * toAlpha).toInt()), ColorUtils.setAlphaComponent(maskColor, (255 * fromAlpha).toInt())), null,
                Shader.TileMode.CLAMP)
    }
}

