package com.nhom1.oxygen.data.service

import com.nhom1.oxygen.data.model.article.OArticle
import com.nhom1.oxygen.data.model.divisions.ODistrict
import com.nhom1.oxygen.data.model.divisions.OProvince
import com.nhom1.oxygen.data.model.divisions.OWard
import com.nhom1.oxygen.data.model.history.OHistory
import com.nhom1.oxygen.data.model.history.OHourlyHistory
import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.data.model.user.OUser
import com.nhom1.oxygen.data.model.user.OUserProfile
import com.nhom1.oxygen.data.model.weather.OWeather
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface OxygenService {
    interface User {
        @GET(OxygenAPI.User.GET_INFO)
        fun getInfo(): Single<OUser>

        data class SetInfoRequest(
            val name: String,
            val profile: OUserProfile
        )

        @PUT(OxygenAPI.User.SET_INFO)
        fun setInfo(
            @Body requestBody: SetInfoRequest
        ): Completable

        data class SetDiseasesRequest(
            val diseases: List<String>
        )

        @PUT(OxygenAPI.User.SET_DISEASES)
        fun setDiseases(
            @Body requestBody: SetDiseasesRequest
        ): Completable

        data class OnSignInResult(
            val name: String,
            val email: String,
            val avatar: String
        )

        @POST(OxygenAPI.User.ON_SIGN_IN)
        fun onSignIn(): Single<OnSignInResult>

        @Multipart
        @POST(OxygenAPI.User.SET_AVATAR)
        fun setAvatar(@Part("file") avatar: MultipartBody.Part): Completable
    }

    val user: User

    interface Weather {
        @GET(OxygenAPI.Weather.GET_CURRENT)
        fun getCurrent(
            @Query("lat") latitude: Double,
            @Query("lon") longitude: Double
        ): Single<OWeather>

        @GET(OxygenAPI.Weather.GET_24H_FORECAST)
        fun get24h(
            @Query("lat") latitude: Double,
            @Query("lon") longitude: Double
        ): Single<List<OWeather>>

        @GET(OxygenAPI.Weather.GET_7D_FORECAST)
        fun get7d(
            @Query("lat") latitude: Double,
            @Query("lon") longitude: Double
        ): Single<List<OWeather>>
    }

    val weather: Weather

    interface Article {
        @GET(OxygenAPI.Article.GET_ARTICLES)
        fun getArticles(): Single<List<OArticle>>

        @GET(OxygenAPI.Article.FIND_ARTICLES)
        fun findArticles(@Query("keywords") query: String): Single<List<OArticle>>
    }

    val article: Article

    interface Geocoding {
        @GET(OxygenAPI.Geocoding.GET_LOCATION)
        fun getLocation(
            @Query("lat") latitude: Double,
            @Query("lon") longitude: Double,
        ): Single<OLocation>

        @GET(OxygenAPI.Geocoding.SEARCH_LOCATION)
        fun searchLocation(
            @Query("text") query: String
        ): Single<List<OLocation>>
    }

    val geocoding: Geocoding

    interface History {
        @GET(OxygenAPI.History.GET_HISTORY_TODAY)
        fun getHistoryToday(): Single<List<OHourlyHistory>>

        @GET(OxygenAPI.History.GET_HISTORY_7D)
        fun getHistory7d(): Single<List<OHistory>>

        @POST(OxygenAPI.History.ADD_HISTORY)
        fun addHistory(@Body requestBody: OHourlyHistory): Completable
    }

    val history: History

    interface Division {
        @GET(OxygenAPI.Division.GET_PROVINCES)
        fun getProvinces(): Single<List<OProvince>>

        @GET(OxygenAPI.Division.GET_DISTRICTS)
        fun getDistricts(@Query("codename") provinceId: String): Single<List<ODistrict>>

        @GET(OxygenAPI.Division.GET_WARDS)
        fun getWards(@Query("codename") districtId: String): Single<List<OWard>>
    }

    val division: Division
}