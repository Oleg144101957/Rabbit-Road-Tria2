package com.rab.bit.road105.di

import android.content.Context
import com.rab.bit.road105.model.repo.DataStoreRepository
import com.rab.bit.road105.model.repo.NetworkCheckerRepository
import com.rab.bit.road105.model.repo.impl.DataStoreRepositoryImpl
import com.rab.bit.road105.model.repo.impl.NetworkCheckerRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideNetwork(@ApplicationContext context: Context): NetworkCheckerRepository {
        return NetworkCheckerRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStoreRepository {
        return DataStoreRepositoryImpl(context)
    }
}