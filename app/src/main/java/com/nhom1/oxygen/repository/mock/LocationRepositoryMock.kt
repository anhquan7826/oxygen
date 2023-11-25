package com.nhom1.oxygen.repository.mock

import com.nhom1.oxygen.data.model.location.OLocation
import com.nhom1.oxygen.repository.LocationRepository
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

class LocationRepositoryMock : LocationRepository {
    override fun getCurrentLocation(): Single<OLocation> {
        return Single.create {
            it.onSuccess(
                OLocation(
                    name = "Vietnam National University – Hoa Lac Campus",
                    city = "Hà Nội",
                    country = "Vietnam",
                    countryCode = "vn",
                    district = "Thach That District",
                    latitude = 21.0039985,
                    longitude = 105.50181441601865,
                    street = "Đường Làng Văn hóa",
                    suburb = "Thạch Hòa"
                )
            )
        }.delay(1000, TimeUnit.MILLISECONDS)
    }

    override fun findLocation(query: String): Single<List<OLocation>> {
        return Single.create {
            it.onSuccess(
                listOf(
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        city = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        suburb = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        city = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        suburb = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        city = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        suburb = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        city = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        suburb = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        city = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        suburb = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        city = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        suburb = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        city = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        suburb = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        city = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        suburb = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        city = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        suburb = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        city = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        suburb = "Thạch Hòa"
                    ),
                )
            )
        }.delay(1000, TimeUnit.MILLISECONDS)
    }
}