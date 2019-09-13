package com.uvtech.makerwala.models

data class DownloadListModel(
    var id: Int = 0,
    var userId: Int = 0,
    var name: String = "",
    var status: Int = 0,//status 0 means not downloaded, 1 means downloaded
    var videoUrl: String = "",
    var videoExtension: String = "",
    var imageUrl: String = "",
    var videoFileName: String = "",
    var imageFileName: String = "",
    var downloaderStatus: Int = 0,
    var autoStarts: Int = 0
) {
    override fun toString(): String {
        return "DownloadListModel(id=$id, userId=$userId, name='$name', status=$status, videoUrl='$videoUrl', videoExtension='$videoExtension', imageUrl='$imageUrl', videoFileName='$videoFileName', imageFileName='$imageFileName', downloaderStatus=$downloaderStatus)"
    }
}