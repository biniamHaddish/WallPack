/*
 * DayTime:9/16/18 12:28 AM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import java.lang.Math.max
import java.lang.Math.min

class BottomNavigationBehavior<V : View>(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<V>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        if (dependency is Snackbar.SnackbarLayout) {
            updateSnackbar(child, dependency)
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onStartNestedScroll(
            coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
            coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        child.translationY = max(0f, min(child.height.toFloat(), child.translationY + dy))
    }

    private fun updateSnackbar(child: View, snackbarLayout: Snackbar.SnackbarLayout) {
        if (snackbarLayout.layoutParams is CoordinatorLayout.LayoutParams) {
            val params = snackbarLayout.layoutParams as CoordinatorLayout.LayoutParams

            params.anchorId = child.id
            params.anchorGravity = Gravity.TOP
            params.gravity = Gravity.TOP
            snackbarLayout.layoutParams = params
        }
    }
}
