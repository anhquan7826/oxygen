package com.nhom1.oxygen.repository.mock

import com.nhom1.oxygen.repository.SuggestionRepository
import io.reactivex.rxjava3.core.Single

class SuggestionRepositoryMock : SuggestionRepository {
    override fun getShortSuggestion(): Single<String> {
        return Single.create {
            it.onSuccess(
                "Nên hạn chế hoạt động ngoài trời, đặc biệt vào buổi trưa. Sử dụng máy lọc không khí trong nhà và giữ cửa sổ kín để tránh khói và bụi. Nếu cần phải ra ngoài, đeo khẩu trang N95 để bảo vệ đường hô hấp."
            )
        }
    }

    override fun getLongSuggestion(): Single<List<String>> {
        return Single.create {
            it.onSuccess(
                List(4) {
                    "Hạn chế hoạt động ngoài trời, đặc biệt vào buổi trưa."
                }
            )
        }
    }
}