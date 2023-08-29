package com.project.fat.data.dto

data class GetHistoryResponse (
    val id: Long,
    val user: GetHistoryUser,
    val date: String,
    val monsterNum: Long,
    val distance: Long
)

data class GetHistoryUser (
    val createdAt: String,
    val updatedAt: String,
    val id: Long,
    val email: String,
    val name: String,
    val role: String? = null,
    val nickname: String,
    val birth: String,
    val authProvider: String? = null,
    val address: String,
    val activated: Boolean
)
