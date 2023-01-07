package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.domain.model.Banner
import com.yapp.itemfinder.domain.repository.BannerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BannerMockRepositoryImpl @Inject constructor() : BannerRepository {

    override fun getAllBanner(): List<Banner> {
        return List(5) {
            Banner(content = "배너 콘텐츠 $it")
        }
    }
}