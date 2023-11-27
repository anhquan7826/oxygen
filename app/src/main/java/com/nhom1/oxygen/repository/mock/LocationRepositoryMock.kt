package com.nhom1.oxygen.repository.mock

import com.nhom1.oxygen.data.model.divisions.ODistrict
import com.nhom1.oxygen.data.model.divisions.OProvince
import com.nhom1.oxygen.data.model.divisions.OWard
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
                    province = "Hà Nội",
                    country = "Vietnam",
                    countryCode = "vn",
                    district = "Thach That District",
                    latitude = 21.0039985,
                    longitude = 105.50181441601865,
                    street = "Đường Làng Văn hóa",
                    ward = "Thạch Hòa"
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
                        province = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        ward = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        province = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        ward = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        province = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        ward = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        province = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        ward = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        province = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        ward = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        province = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        ward = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        province = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        ward = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        province = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        ward = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        province = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        ward = "Thạch Hòa"
                    ),
                    OLocation(
                        name = "Vietnam National University – Hoa Lac Campus",
                        province = "Hà Nội",
                        country = "Vietnam",
                        countryCode = "vn",
                        district = "Thach That District",
                        latitude = 21.0039985,
                        longitude = 105.50181441601865,
                        street = "Đường Làng Văn hóa",
                        ward = "Thạch Hòa"
                    ),
                )
            )
        }.delay(1000, TimeUnit.MILLISECONDS)
    }

    override fun getProvinces(): Single<List<OProvince>> {
        return Single.create {
            it.onSuccess(
                listOf(
                    OProvince("ha_noi", "Thành phố Hà Nội"),
                    OProvince("ho_chi_minh", "Thành phố Hồ Chí Minh"),
                    OProvince("thanh_hoa", "Thanh Hóa")
                )
            )
        }.delay(250, TimeUnit.MILLISECONDS)
    }

    override fun getDistricts(provinceId: String): Single<List<ODistrict>> {
        return Single.create {
            it.onSuccess(
                listOf(
                    ODistrict("quan_cau_giay", "Quận Cầu Giấy"),
                    ODistrict("quan_thanh_xuan", "Quận Thanh Xuân"),
                )
            )
        }.delay(250, TimeUnit.MILLISECONDS)
    }

    override fun getWards(districtId: String): Single<List<OWard>> {
        return Single.create {
            it.onSuccess(
                listOf(
                    OWard("phuong_mai_dich", "Phường Mai Dịch"),
                    OWard("phuong_dich_vong_hay", "Phường Dịch Vọng Hậu"),
                )
            )
        }.delay(250, TimeUnit.MILLISECONDS)
    }
}