package com.nhom1.oxygen.data.service

object OxygenAPI {
    const val baseUrl = "http://192.168.0.108:5000/api/v1/"

    object User {
        const val SET_DISEASES = "users/diseases"
        const val GET_INFO = "users"
        const val SET_INFO = "users"
        const val ON_SIGN_IN = "users"
        const val SET_AVATAR = "users/avatar"
    }

    object Weather {
        const val GET_CURRENT = "weather/current"
        const val GET_24H_FORECAST = "weather/forecast24h"
        const val GET_7D_FORECAST = "weather/forecast7d"
    }

    object Article {
        const val GET_ARTICLES = "article"
        const val FIND_ARTICLES = "article/find"
    }

    object Geocoding {
        const val GET_LOCATION = "geocode/reverse-geocoding"
        const val SEARCH_LOCATION = "geocode/related-location"
    }

    object History {
        const val GET_HISTORY_TODAY = "locations/history/today"
        const val GET_HISTORY_7D = "locations/history/7days"
        const val ADD_HISTORY = "locations/history"
    }

    object Division {
        const val GET_PROVINCES = "locationlist/cities"
        const val GET_DISTRICTS = "locationlist/districts"
        const val GET_WARDS = "locationlist/wards"
    }
}