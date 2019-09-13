package com.uvtech.makerwala.models

data class HomeListModel(
    var id: Int = 0,
    var videoId: String = "",
    var title: String = "",
    var publicDetail: String = "",
    var privateDetail: String = "",
    var rate: Float = 0F,
    var isPrivate: Boolean = false
)
