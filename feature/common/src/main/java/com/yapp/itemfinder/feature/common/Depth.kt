package com.yapp.itemfinder.feature.common

import androidx.annotation.ColorRes

enum class Depth(@ColorRes val colorId: Int) {
    FIRST(R.color.white), SECOND(R.color.brown_02), THIRD(R.color.brown_03), SETTINGS(R.color.brown_01);
}
