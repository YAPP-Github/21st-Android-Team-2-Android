package com.yapp.itemfinder.domain.usecase

import com.yapp.itemfinder.domain.model.Banner
import com.yapp.itemfinder.domain.repository.BannerRepository
import javax.inject.Inject

class GetBannersUseCase @Inject constructor(
    private val bannerRepository: BannerRepository
    ) {

    operator fun invoke(): List<Banner> {
        return bannerRepository.getAllBanner()
    }
}