package com.yapp.itemfinder.space.additem.addtag

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.domain.repository.TagRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.space.additem.AddItemActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTagViewModel @Inject constructor(
    private val tagRepository: TagRepository,
    val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<AddTagState, AddTagSideEffect>() {

    override val _stateFlow: MutableStateFlow<AddTagState> =
        MutableStateFlow(AddTagState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<AddTagSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(AddTagState.Loading)
        val selectedTagList = savedStateHandle.get<List<Tag>>(AddItemActivity.SELECTED_TAGS_KEY)
        setState(
            AddTagState.Success(
                selectedTagList = selectedTagList,
                dataList = tagRepository.fetchTags()
            )
        )
    }

    fun deselectTag(tag: Tag) {
        withState<AddTagState.Success> { state ->
            setState(
                state.copy(
                    selectedTagList = state.selectedTagList?.toMutableList()?.apply {
                        remove(tag)
                    }
                )
            )
        }
    }

    fun selectTag(tag: Tag) {
        withState<AddTagState.Success> { state ->
            state.selectedTagList?.find { it.name == tag.name }?.let {
                postSideEffect(
                    AddTagSideEffect.ShowToast("이미 선택 된 태그입니다.")
                )
            } ?: setState(
                state.copy(
                    selectedTagList = state.selectedTagList?.toMutableList()?.apply {
                        add(tag)
                    }
                )
            )
        }
    }

    fun addTag(tagName: String) = viewModelScope.launch {
        val createTags = tagRepository.createTags(listOf(tagName))
        withState<AddTagState.Success> { state ->
            setState(
                state.copy(
                    dataList = state.dataList.toMutableList().apply {
                        addAll(createTags)
                    }
                )
            )
        }
    }

    fun saveTags() {
        withState<AddTagState.Success> { state ->
            postSideEffect(
                AddTagSideEffect.Save(
                    state.selectedTagList ?: listOf()
                )
            )
        }
    }


}
