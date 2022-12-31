package com.yapp.itemfinder.feature.common.datalist.binder

import android.annotation.SuppressLint
import com.yapp.itemfinder.domain.model.CellType
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.LikeItem
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.di.ContainerLikeItemQualifier
import com.yapp.itemfinder.feature.common.datalist.binder.di.HomeLikeItemQualifier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataBindHelper @Inject constructor(
    @HomeLikeItemQualifier
    private val homeLikeItemBinder: LikeItemBinder,
    @ContainerLikeItemQualifier
    private val containerLikeItemBinder: LikeItemBinder,
) {

    @SuppressLint("CheckResult")
    fun bindList(dataList: List<Data>, viewModel: BaseViewModel) {
        dataList.forEach { data ->
            bindData(data, viewModel)
        }
    }

    private fun bindData(data: Data, viewModel: BaseViewModel) {
        when(data.type) {
            CellType.LIKE_CELL -> {
                homeLikeItemBinder.bindData(data as LikeItem, viewModel)
                containerLikeItemBinder.bindData(data as LikeItem, viewModel)
            }
            else -> { }
        }
    }

}
