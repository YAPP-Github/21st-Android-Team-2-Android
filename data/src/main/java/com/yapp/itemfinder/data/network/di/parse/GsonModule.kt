package com.yapp.itemfinder.data.network.di.parse

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.yapp.itemfinder.domain.di.ApiGsonQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GsonModule {

    @ApiGsonQualifier
    @Provides
    fun provideApiGson() = GsonBuilder()
        .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setLenient()
        .create()

}
