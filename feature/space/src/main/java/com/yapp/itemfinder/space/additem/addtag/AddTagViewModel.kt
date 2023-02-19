package com.yapp.itemfinder.space.additem.addtag

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.LockerRepositoryQualifiers
import com.yapp.itemfinder.data.repositories.di.SpaceRepositoryQualifiers
import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.domain.repository.ImageRepository
import com.yapp.itemfinder.domain.repository.LockerRepository
import com.yapp.itemfinder.domain.repository.SpaceRepository
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
    @LockerRepositoryQualifiers
    private val lockerRepository: LockerRepository,
    private val imageRepository: ImageRepository,
    @SpaceRepositoryQualifiers
    private val spaceRepository: SpaceRepository,
    val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<AddTagState, AddTagSideEffect>() {

    override val _stateFlow: MutableStateFlow<AddTagState> =
        MutableStateFlow(AddTagState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<AddTagSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(AddTagState.Loading)
        val selectedTagList = savedStateHandle.get<List<Tag>>(AddItemActivity.SELECTED_TAGS_KEY)
    }

}
