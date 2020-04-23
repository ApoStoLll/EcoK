package com.missclick.eco

import android.content.Context
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator


class FAB_Float_on_Scroll(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior() {

    override fun onNestedScroll(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)

        //child -> Floating Action Button
        if (dyConsumed > 0) {
            val layoutParams = child.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
            val fab_bottomMargin = layoutParams.bottomMargin
            child.animate().translationY((child.height + fab_bottomMargin).toFloat())
                .setInterpolator(LinearInterpolator()).start()
        } else if (dyConsumed < 0) {
            child.animate().translationY(0f).setInterpolator(LinearInterpolator()).start()
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int
    ): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
}