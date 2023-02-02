package com.yapp.itemfinder.feature.common.extension

import android.view.View
import androidx.annotation.Px
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.*
import androidx.constraintlayout.widget.ConstraintSet

fun ConstraintLayout.applyConstraint(block: ConstraintSet.() -> Unit) {
    ConstraintSet().apply {
        clone(this@applyConstraint)
        block(this)
    }.applyTo(this)
}

fun ConstraintSet.centerParent(v1: View) {
    centerHorizontallyParent(v1)
    centerVerticallyParent(v1)
}

fun ConstraintSet.centerHorizontallyParent(v1: View) {
    centerHorizontally(v1.id, PARENT_ID)
}

fun ConstraintSet.centerVerticallyParent(v1: View) {
    centerVertically(v1.id, PARENT_ID)
}

fun ConstraintSet.horizontalBias(v1: View, percentage: Float) {
    setHorizontalBias(v1.id, percentage)
}

fun ConstraintSet.verticalBias(v1: View, percentage: Float) {
    setVerticalBias(v1.id, percentage)
}

fun ConstraintSet.topToTop(v1: View, v2: View, @Px margin: Int = 0) {
    connect(v1.id, TOP, v2.id, TOP, margin)
}

fun ConstraintSet.topToParent(v1: View, @Px margin: Int = 0) {
    connect(v1.id, TOP, PARENT_ID, TOP, margin)
}

fun ConstraintSet.bottomToBottom(v1: View, v2: View, @Px margin: Int = 0) {
    connect(v1.id, BOTTOM, v2.id, BOTTOM, margin)
}

fun ConstraintSet.bottomToParent(v1: View, @Px margin: Int = 0) {
    connect(v1.id, BOTTOM, PARENT_ID, BOTTOM, margin)
}

fun ConstraintSet.topToBottom(v1: View, v2: View, @Px margin: Int = 0) {
    connect(v1.id, TOP, v2.id, BOTTOM, margin)
}

fun ConstraintSet.bottomToTop(v1: View, v2: View, @Px margin: Int = 0) {
    connect(v1.id, BOTTOM, v2.id, TOP, margin)
}

fun ConstraintSet.startToStart(v1: View, v2: View, @Px margin: Int = 0) {
    connect(v1.id, START, v2.id, START, margin)
}

fun ConstraintSet.startToParent(v1: View, @Px margin: Int = 0) {
    connect(v1.id, START, PARENT_ID, START, margin)
}

fun ConstraintSet.endToEnd(v1: View, v2: View, @Px margin: Int = 0) {
    connect(v1.id, END, v2.id, END, margin)
}

fun ConstraintSet.endToParent(v1: View, @Px margin: Int = 0) {
    connect(v1.id, END, PARENT_ID, END, margin)
}

fun ConstraintSet.startToEnd(v1: View, v2: View, @Px margin: Int = 0) {
    connect(v1.id, START, v2.id, END, margin)
}

fun ConstraintSet.endToStart(v1: View, v2: View, @Px margin: Int = 0) {
    connect(v1.id, END, v2.id, START, margin)
}
