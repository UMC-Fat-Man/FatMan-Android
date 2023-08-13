package com.project.fat.data.dto

import java.sql.Timestamp

data class UserFatman(
    val id : Long,
    val userId : Long,
    val fatmanId : Long,
    var activated : Boolean,
    var updatedAt : Timestamp
)
