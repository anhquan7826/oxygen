package com.nhom1.oxygen.repository.mock

import com.nhom1.oxygen.data.model.article.OArticle
import com.nhom1.oxygen.repository.ArticleRepository
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

class ArticleRepositoryMock : ArticleRepository {
    override fun getArticle(): Single<List<OArticle>> {
        return Single.create {
            it.onSuccess(listOf(
                OArticle(
                    title = "Sương mù dày đặc trên nhiều tuyến đường trung tâm Hà Nội",
                    preview = "Sáng 3-4, tại nhiều tuyến đường trung tâm Hà Nội sương mù dày đặc, xe cộ phải bật đèn chiếu sáng. Các trạm quan trắc môi trường cho hay chất lượng không khí ở ngưỡng tốt và trung bình.",
                    image = "https://cdn.tuoitre.vn/zoom/260_163/471584752817336320/2023/4/3/nen-16804869189691236796474-46-0-1296-2000-crop-1680486925522427828904.jpg",
                    url = "https://tuoitre.vn/suong-mu-day-dac-tren-nhieu-tuyen-duong-trung-tam-ha-noi-20230403090038359.htm"
                ),
                OArticle(
                    title = "Sương mù dày đặc trên nhiều tuyến đường trung tâm Hà Nội",
                    preview = "Sáng 3-4, tại nhiều tuyến đường trung tâm Hà Nội sương mù dày đặc, xe cộ phải bật đèn chiếu sáng. Các trạm quan trắc môi trường cho hay chất lượng không khí ở ngưỡng tốt và trung bình.",
                    image = "https://cdn.tuoitre.vn/zoom/260_163/471584752817336320/2023/4/3/nen-16804869189691236796474-46-0-1296-2000-crop-1680486925522427828904.jpg",
                    url = "https://tuoitre.vn/suong-mu-day-dac-tren-nhieu-tuyen-duong-trung-tam-ha-noi-20230403090038359.htm"
                ),
                OArticle(
                    title = "Sương mù dày đặc trên nhiều tuyến đường trung tâm Hà Nội",
                    preview = "Sáng 3-4, tại nhiều tuyến đường trung tâm Hà Nội sương mù dày đặc, xe cộ phải bật đèn chiếu sáng. Các trạm quan trắc môi trường cho hay chất lượng không khí ở ngưỡng tốt và trung bình.",
                    image = "https://cdn.tuoitre.vn/zoom/260_163/471584752817336320/2023/4/3/nen-16804869189691236796474-46-0-1296-2000-crop-1680486925522427828904.jpg",
                    url = "https://tuoitre.vn/suong-mu-day-dac-tren-nhieu-tuyen-duong-trung-tam-ha-noi-20230403090038359.htm"
                ),
            ))
        }.delay(1000, TimeUnit.MILLISECONDS)
    }

    override fun findArticle(query: String): Single<List<OArticle>> {
        return Single.create {
            it.onSuccess(
                listOf(
                    OArticle(
                        title = "Sương mù dày đặc trên nhiều tuyến đường trung tâm Hà Nội",
                        preview = "Sáng 3-4, tại nhiều tuyến đường trung tâm Hà Nội sương mù dày đặc, xe cộ phải bật đèn chiếu sáng. Các trạm quan trắc môi trường cho hay chất lượng không khí ở ngưỡng tốt và trung bình.",
                        image = "https://cdn.tuoitre.vn/zoom/260_163/471584752817336320/2023/4/3/nen-16804869189691236796474-46-0-1296-2000-crop-1680486925522427828904.jpg",
                        url = "https://tuoitre.vn/suong-mu-day-dac-tren-nhieu-tuyen-duong-trung-tam-ha-noi-20230403090038359.htm"
                    ),
                    OArticle(
                        title = "Sương mù dày đặc trên nhiều tuyến đường trung tâm Hà Nội",
                        preview = "Sáng 3-4, tại nhiều tuyến đường trung tâm Hà Nội sương mù dày đặc, xe cộ phải bật đèn chiếu sáng. Các trạm quan trắc môi trường cho hay chất lượng không khí ở ngưỡng tốt và trung bình.",
                        image = "https://cdn.tuoitre.vn/zoom/260_163/471584752817336320/2023/4/3/nen-16804869189691236796474-46-0-1296-2000-crop-1680486925522427828904.jpg",
                        url = "https://tuoitre.vn/suong-mu-day-dac-tren-nhieu-tuyen-duong-trung-tam-ha-noi-20230403090038359.htm"
                    ),
                    OArticle(
                        title = "Sương mù dày đặc trên nhiều tuyến đường trung tâm Hà Nội",
                        preview = "Sáng 3-4, tại nhiều tuyến đường trung tâm Hà Nội sương mù dày đặc, xe cộ phải bật đèn chiếu sáng. Các trạm quan trắc môi trường cho hay chất lượng không khí ở ngưỡng tốt và trung bình.",
                        image = "https://cdn.tuoitre.vn/zoom/260_163/471584752817336320/2023/4/3/nen-16804869189691236796474-46-0-1296-2000-crop-1680486925522427828904.jpg",
                        url = "https://tuoitre.vn/suong-mu-day-dac-tren-nhieu-tuyen-duong-trung-tam-ha-noi-20230403090038359.htm"
                    ),
                )
            )
        }.delay(1000, TimeUnit.MILLISECONDS)
    }
}