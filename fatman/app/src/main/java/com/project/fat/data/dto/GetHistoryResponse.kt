package com.project.fat.data.dto

import java.time.LocalDate

typealias GetHistoryResponse = ArrayList<GetHistoryResponseElement>

data class GetHistoryResponseElement (
    val id: Long,
    val user: User,
    val date: LocalDate,
    val monsterNum: Long,
    val distance: Double
)

data class User (
    val createdAt: String,
    val updatedAt: String,
    val id: Long,
    val email: String,
    val name: String,
    val nickname: String,
    val birth: String,
    val social: Any? = null,
    val address: String,
    val activated: Boolean
)
