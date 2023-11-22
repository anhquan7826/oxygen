package com.nhom1.oxygen.data.model.article

import com.google.gson.annotations.SerializedName

data class OArticle(
    val title: String,
    @SerializedName("imgSrc") val image: String,
    @SerializedName("preViewText") val preview: String,
    val url: String,
)
