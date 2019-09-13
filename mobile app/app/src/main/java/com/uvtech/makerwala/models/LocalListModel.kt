package com.uvtech.makerwala.models

data class LocalListModel(
    var id: Int = 0,
    var name: String = "",
    var status: Int = 0,//status 0 means not downloaded, 1 means downloaded
    var videoUrl: String = "",
    var videoExtension: String = "",
    var imageUrl: String = "",
    var videoFileName: String = "",
    var imageFileName: String = "",
    var downloaderStatus: Int = 0
)