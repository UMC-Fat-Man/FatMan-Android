package com.project.fat.data.dto

import java.sql.Timestamp

data class Monster (
    val id : Long,
    val monsterName : String,
    val monsterImageUrl : String,
    val createdAt : Timestamp,
    val updatedAt : Timestamp
)