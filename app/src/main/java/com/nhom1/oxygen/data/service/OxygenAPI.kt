package com.nhom1.oxygen.data.service

object OxygenAPI {
    const val baseUrl = "https://oxygen.radrat.vn/api/v1"

    object User {
        const val SET_DISEASES = "/users/diseases"
        const val GET_INFO = "/users"
        const val SET_INFO = "/users"
    }

    object Weather {
        const val GET_CURRENT = "/weather/current"
        const val GET_24H_FORECAST = "/weather/forecast24h"
        const val GET_3D_FORECAST = "/weather/forecast3d"
        const val GET_7D_FORECAST = "/weather/forecast7d"
        const val GET_10D_FORECAST = "/weather/forecast10d"
    }

    object Article {
        const val GET_ARTICLES = "/article"
        const val FIND_ARTICLES = "/article/find"
    }

    object Geocoding {
        const val GET_LOCATION = "/geocode/reverse-geocoding"
        const val SEARCH_LOCATION = "/geocode/related-location"
    }

    object History {
        const val GET_HISTORY = "/locations/history"
        const val ADD_HISTORY = "/locations/history"
    }
}