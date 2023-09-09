package com.project.fat.data.dto

data class CreateHistoryResponse (
    val monsterNum: Long,
    val distance: Long,
    val id: Long,
    val user: CreateHistoryUser,
    val date: String
)

data class CreateHistoryUser (
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