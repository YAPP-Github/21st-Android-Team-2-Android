package com.yapp.itemfinder.space.lockerdetail.itemfilter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.ItemCategory
import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.domain.repository.TagRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.space.lockerdetail.itemfilter.ItemFilterActivity.Companion.INITIAL_ITEM_FILTER_CONDITION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemFilterViewModel @Inject constructor(
    private val tagRepository: TagRepository,
    val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<ItemFilterState, ItemFilterSideEffect>() {

    override val _stateFlow: MutableStateFlow<ItemFilterState> =
        MutableStateFlow(ItemFilterState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<ItemFilterSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(ItemFilterState.Loading)
        val itemFilterCondition = savedStateHandle.get<ItemFilterCondition>(INITIAL_ITEM_FILTER_CONDITION) ?: ItemFilterCondition.NONE
        setState(
            ItemFilterState.Success(
                initialItemFilterCondition = itemFilterCondition,
                appliedItemFilterCondition = itemFilterCondition.copy(),
                tagList = tagRepository.fetchTags(),
                isRefreshNeed = true
            )
        )
    }

    fun selectTag(tagId: Long) {
        withState<ItemFilterState.Success> { state ->
            val tags = state.appliedItemFilterCondition.tags.toMutableList()
            setState(
                state.copy(
                    appliedItemFilterCondition = state.appliedItemFilterCondition.copy(
                        tags = tags.apply {
                            state.tagList.find { it.id == tagId }?.let {
                                add(it)
                            }
                        }
                    ),
                    isRefreshNeed = false
                )
            )
        }
    }

    fun deselectTag(tag: Tag) {
        withState<ItemFilterState.Success> { state ->
            val tags = state.appliedItemFilterCondition.tags.toMutableList()
            setState(
                state.copy(
                    appliedItemFilterCondition = state.appliedItemFilterCondition.copy(
                        tags = tags.apply {
                            remove(tag)
                        }
                    ),
                    isRefreshNeed = false
                )
            )
        }
    }

    fun selectOrder(order: ItemFilterCondition.Order) {
        withState<ItemFilterState.Success> { state ->
            setState(
                state.copy(
                    appliedItemFilterCondition = state.appliedItemFilterCondition.copy(
                        order = order
                    ),
                    isRefreshNeed = false
                )
            )
        }
    }

    fun checkCategory(itemCategories: List<ItemCategory>) {
        withState<ItemFilterState.Success> { state ->
            setState(
                state.copy(
                    appliedItemFilterCondition = state.appliedItemFilterCondition.copy(
                        itemCategoris = itemCategories
                    ),
                    isRefreshNeed = false
                )
            )
        }
    }

    fun applyFilterCondition() {
        withState<ItemFilterState.Success> { state ->
            postSideEffect(
                ItemFilterSideEffect.Apply(state.appliedItemFilterCondition)
            )
        }
    }

    fun resetFilterCondition() {
        withState<ItemFilterState.Success> { state ->
            val initialItemFilterCondition = state.initialItemFilterCondition.copy()
            setState(
                state.copy(
                    appliedItemFilterCondition = initialItemFilterCondition,
                    isRefreshNeed = false
                )
            )
            postSideEffect(
                ItemFilterSideEffect.Reset(initialItemFilterCondition)
            )
        }
    }


}
