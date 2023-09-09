package com.project.fat.data.dto

typealias GetHistoryResponse = ArrayList<GetHistoryResponseElement>

data class GetHistoryResponseElement (
    val id: Long,
    val user: User,
    val date: String,
    val monsterNum: Long,
    val distance: Long
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
