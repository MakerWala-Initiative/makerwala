package com.uvtech.makerwala.models

class DownloadLinkModel(
    var id: Int,
    var size: Int = 0,
    var link: String = "",
    var extension: String = "",
    var height: String = "",
    var isNotAvailable: Boolean = false
) {

    override fun toString(): String {
        return "DownloadLinkModel(id=$id, size=$size, link='$link', extension='$extension', height='$height', isNotAvailable=$isNotAvailable)"
    }
}
