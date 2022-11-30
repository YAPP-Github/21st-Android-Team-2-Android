package com.yapp.itemfinder.utility.di

import com.yapp.itemfinder.feature.common.utility.ScreenNavigator
import com.yapp.itemfinder.utility.DefaultScreenNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ScreenNavigatorModule {

    @Binds
    @Singleton
    abstract fun bindScreenNavigator(
        defaultScreenNavigator: DefaultScreenNavigator,
    ): ScreenNavigator
}
