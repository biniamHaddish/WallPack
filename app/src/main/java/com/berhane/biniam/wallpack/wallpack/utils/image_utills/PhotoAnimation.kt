/*
 * DayTime:9/13/18 12:24 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils.image_utills

import android.animation.ObjectAnimator
import com.bumptech.glide.request.transition.ViewPropertyTransition

class PhotoAnimation {

    companion object {
        fun newInstance() = PhotoAnimation()
    }


    fun fadeAnimation(): ViewPropertyTransition.Animator {
        val fadeAnimation = ViewPropertyTransition.Animator { view ->
            val fadeAnimation = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
            fadeAnimation.duration = 1000// 500 is good for the eye
            fadeAnimation.start()
        }
        return fadeAnimation
    }


//    fun colorAnimator(context: Context) {
//        val colorFrom = ColorShifter.computeCardBackgroundColor(context, context.color)
//        val colorTo: Int
//        if (context.color != null) {
//            colorTo = Color.parseColor(wallPackPhotos.color)
//        } else {
//            colorTo = colorFrom
//        }
//        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
//
//    }
}