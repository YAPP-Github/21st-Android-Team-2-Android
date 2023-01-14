package com.yapp.itemfinder.domain.model

data class Thing(
    override var id: Long,
    var lockerId: Long,
    var name: String,
    var expirationDate: String?,
    var imageUrl: String?,
    var tags: List<Tag>?

) : Data(){

}
