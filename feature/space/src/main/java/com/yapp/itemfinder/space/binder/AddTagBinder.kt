package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.TagBinder
import com.yapp.itemfinder.space.additem.addtag.AddTagViewModel
import javax.inject.Inject

class AddTagBinder @Inject constructor() : TagBinder {

    override fun bindData(data: Tag, viewModel: BaseViewModel) {
        when(viewModel){
            is AddTagViewModel -> {
                setLockerDetailViewModelHandlers(data, viewModel)
            }
        }

    }

    private fun setLockerDetailViewModelHandlers(data: Tag, vm: AddTagViewModel){
        data.tagHandler = {
            vm.selectTag(it)
        }
    }

}
