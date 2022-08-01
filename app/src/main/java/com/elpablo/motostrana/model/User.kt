package com.elpablo.motostrana.model

data class User(
    val id: String = "",
    var phone: String = "",
    var nickname: String = "",
    var fullname: String = "Имя Фамилия",
    var photo: String = "",
    var status: String = ""
)
