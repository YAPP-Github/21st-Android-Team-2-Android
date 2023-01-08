package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.Banner


interface BannerRepository {
    fun getAllBanner(): List<Banner>
}