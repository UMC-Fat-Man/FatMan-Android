package com.project.fat.data.dto

import com.google.gson.annotations.SerializedName
import retrofit2.http.Header
import java.util.Date

data class SignInResponse(
    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("birth")
    val birth: String,

    @SerializedName("social")
    val social: Boolean,

    @SerializedName("address")
    val address: String,

    @SerializedName("activated")
    val activated: Boolean,

    @Header("Refresh-Token")
    val refresh_token: String,

    @Header("Access-Token")
    val access_token: String
)
