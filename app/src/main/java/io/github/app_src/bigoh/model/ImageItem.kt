package io.github.app_src.bigoh.model

data class ImageItem(
    val id: Int,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val downloadUrl: String
)