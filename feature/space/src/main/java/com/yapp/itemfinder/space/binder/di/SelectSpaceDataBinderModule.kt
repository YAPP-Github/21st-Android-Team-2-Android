package com.yapp.itemfinder.space.binder.di

import com.yapp.itemfinder.feature.common.datalist.binder.SelectSpaceBinder
import com.yapp.itemfinder.space.binder.SelectSpaceBinderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SelectSpaceDataBinderModule {
    @Binds
    @Singleton
    abstract fun bindSelectSpaceBinder(
        selectSpaceBinder: SelectSpaceBinderImpl
    ): SelectSpaceBinder
}
