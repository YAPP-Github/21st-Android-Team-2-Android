package com.yapp.itemfinder.feature.common.views.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CustomDraggableBottomSheetBehaviour<V: View>: BottomSheetBehavior<V> {

   var draggableView: View? = null

   constructor(): super()
   constructor(ctx: Context, attrs: AttributeSet): super(ctx, attrs)

   override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
      draggableView?.let {
         isDraggable = parent.isPointInChildBounds(it, event.x.toInt(), event.y.toInt())
      }
      return super.onInterceptTouchEvent(parent, child, event)
   }
}
