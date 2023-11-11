package com.nhom1.oxygen.dep_inject

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.nhom1.oxygen.repository.AirQualityRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.repository.impl.AirQualityRepositoryImpl
import com.nhom1.oxygen.repository.impl.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    @Singleton
    fun provideAirQualityRepository(): AirQualityRepository {
        return AirQualityRepositoryImpl();
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepositoryImpl(
            firebaseAuth = Firebase.auth
        )
    }
}