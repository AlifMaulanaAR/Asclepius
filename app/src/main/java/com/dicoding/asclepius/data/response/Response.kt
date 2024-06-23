package com.dicoding.asclepius.data.response

import com.google.gson.annotations.SerializedName

data class Responses(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("totalResults")
    val totalResults: Int,

    @field:SerializedName("articles")
    val articles: List<ArticlesItem>
)

data class ArticlesItem (
    @field:SerializedName("author")
    val author: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("description")
    val description: Any,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("urlToImage")
    val urlToImage: Any,

    @field:SerializedName("publishedAt")
    val publishedAt: String,

    @field:SerializedName("content")
    val content: Any
)

