package com.nhom1.oxygen.dep_inject

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.nhom1.oxygen.data.database.OxygenDatabase
import com.nhom1.oxygen.data.service.OxygenAPI
import com.nhom1.oxygen.data.service.OxygenInterceptor
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.ArticleRepository
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.PathologyRepository
import com.nhom1.oxygen.repository.SettingRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.repository.WeatherRepository
import com.nhom1.oxygen.repository.impl.ArticleRepositoryImpl
import com.nhom1.oxygen.repository.impl.HistoryRepositoryImpl
import com.nhom1.oxygen.repository.impl.LocationRepositoryImpl
import com.nhom1.oxygen.repository.impl.SettingRepositoryImpl
import com.nhom1.oxygen.repository.impl.UserRepositoryImpl
import com.nhom1.oxygen.repository.impl.WeatherRepositoryImpl
import com.nhom1.oxygen.repository.mock.PathologyRepositoryMock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object OxygenModule {
    @Provides
    @Singleton
    fun provideOxygenService(@ApplicationContext context: Context): OxygenService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                OxygenInterceptor(
                    context.getSharedPreferences(
                        context.packageName,
                        Context.MODE_PRIVATE
                    )
                )
            )
            .addNetworkInterceptor(loggingInterceptor)
            .build()

        val builder = Retrofit.Builder()
            .baseUrl(OxygenAPI.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        return object : OxygenService {
            override val user: OxygenService.User =
                builder.create(OxygenService.User::class.java)
            override val weather: OxygenService.Weather =
                builder.create(OxygenService.Weather::class.java)
            override val article: OxygenService.Article =
                builder.create(OxygenService.Article::class.java)
            override val geocoding: OxygenService.Geocoding =
                builder.create(OxygenService.Geocoding::class.java)
            override val history: OxygenService.History =
                builder.create(OxygenService.History::class.java)
            override val division: OxygenService.Division =
                builder.create(OxygenService.Division::class.java)
        }
    }

    @Provides
    @Singleton
    fun provideOxygenDatabase(@ApplicationContext context: Context): OxygenDatabase {
        return OxygenDatabase.build(context)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(service: OxygenService): WeatherRepository {
        return WeatherRepositoryImpl(service)
//        return WeatherRepositoryMock()
    }

    @Provides
    @Singleton
    fun provideUserRepository(service: OxygenService): UserRepository {
        return UserRepositoryImpl(
            firebaseAuth = Firebase.auth,
            service = service
        )
//        return UserRepositoryMock()
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        @ApplicationContext context: Context,
        service: OxygenService,
        database: OxygenDatabase
    ): LocationRepository {
        return LocationRepositoryImpl(context, service, database)
//        return LocationRepositoryMock(database)
    }

    @Provides
    @Singleton
    fun provideArticleRepository(service: OxygenService): ArticleRepository {
        return ArticleRepositoryImpl(service)
//        return ArticleRepositoryMock()
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(
        @ApplicationContext context: Context,
        service: OxygenService
    ): HistoryRepository {
        return HistoryRepositoryImpl(context, service)
//        return HistoryRepositoryMock()
    }

    @Provides
    @Singleton
    fun providePathologyRepository(): PathologyRepository {
        return PathologyRepositoryMock()
    }

    @Provides
    @Singleton
    fun provideSettingRepository(@ApplicationContext context: Context): SettingRepository {
        return SettingRepositoryImpl(context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE))
    }
}