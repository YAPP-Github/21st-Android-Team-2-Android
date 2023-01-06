package com.yapp.itemfinder.feature.common.datalist.binder

import android.annotation.SuppressLint
import com.yapp.itemfinder.domain.model.*
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.di.HomeLikeItemQualifier
import com.yapp.itemfinder.feature.common.datalist.binder.di.HomeMySpaceUpperCellItemQualifier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataBindHelper @Inject constructor(
    @HomeLikeItemQualifier
    private val homeLikeItemBinder: LikeItemBinder,
    private val homeSpaceItemBinder: SpaceItemBinder,
    @HomeMySpaceUpperCellItemQualifier
    private val homeMySpaceUpperCellItemBinder: CellItemBinder,
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
            }
            CellType.SPACE_CELL -> {
                homeSpaceItemBinder.bindData(data as SpaceItem, viewModel)
            }
            CellType.HOMETAB_MYSPACE_UPPER_CELL -> {
                homeMySpaceUpperCellItemBinder.bindData(data as CellItem, viewModel)
            }
            else -> { }
        }
    }

}
