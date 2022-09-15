package com.example.adni.di

import com.example.adni.domain.ILocationService
import com.example.adni.framework.LocationService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DomainModule {
    @Singleton
    @Binds
    abstract fun provideLocationService(locationService: LocationService): ILocationService
}