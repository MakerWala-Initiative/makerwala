package com.uvtech.makerwala.models

data class CommentsModel(
    var comments: String = "", var userName: String = "", var imageProfile: String = "", var time: String = "",
    var rating: String = "", var createdBy: Int = 0, var transId: Int = 0, var typeId: Int = 0
)
