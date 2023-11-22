package com.nhom1.oxygen.dep_inject

import android.content.Context
import com.nhom1.oxygen.data.service.OxygenService
import com.nhom1.oxygen.repository.ArticleRepository
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.repository.WeatherRepository
import com.nhom1.oxygen.repository.mock.ArticleRepositoryMock
import com.nhom1.oxygen.repository.mock.HistoryRepositoryMock
import com.nhom1.oxygen.repository.mock.LocationRepositoryMock
import com.nhom1.oxygen.repository.mock.UserRepositoryMock
import com.nhom1.oxygen.repository.mock.WeatherRepositoryMock
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
    private const val baseUrl: String = "http://127.0.0.1:80"

    @Provides
    @Singleton
    fun provideOxygenService(): OxygenService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .build()

        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        return object : OxygenService {
            override val user: OxygenService.User = builder.create(OxygenService.User::class.java)
            override val weather: OxygenService.Weather =
                builder.create(OxygenService.Weather::class.java)
            override val article: OxygenService.Article =
                builder.create(OxygenService.Article::class.java)
            override val geocoding: OxygenService.Geocoding =
                builder.create(OxygenService.Geocoding::class.java)
            override val history: OxygenService.History = builder.create(OxygenService.History::class.java)
        }
    }

    @Provides
    @Singleton
    fun provideAirQualityRepository(service: OxygenService): WeatherRepository {
//        return WeatherRepositoryImpl(service)
        return WeatherRepositoryMock()
    }

    @Provides
    @Singleton
    fun provideUserRepository(service: OxygenService): UserRepository {
//        return UserRepositoryImpl(
//            firebaseAuth = Firebase.auth,
//            service = service
//        )
        return UserRepositoryMock()
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        @ApplicationContext context: Context,
        service: OxygenService
    ): LocationRepository {
//        return LocationRepositoryImpl(context, service)
        return LocationRepositoryMock()
    }

    @Provides
    @Singleton
    fun provideArticleRepository(service: OxygenService): ArticleRepository {
//        return ArticleRepositoryImpl(service)
        return ArticleRepositoryMock()
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(@ApplicationContext context: Context, service: OxygenService): HistoryRepository {
//        return HistoryRepositoryImpl(context, service)
        return HistoryRepositoryMock()
    }
}