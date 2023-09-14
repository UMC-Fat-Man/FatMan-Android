package com.project.fat.data.dto

import com.google.gson.annotations.SerializedName

//typealias WeekRankResponseModel = ArrayList<WeekRankResponse>

data class WeekRankResponseModel(
    @SerializedName("id")
    val id: String,

    @SerializedName("monsterNum")
    val monsterNum: Int,

    @SerializedName("distance")
    val distance: Float,

    @SerializedName("user")
    val user: SignInResponse,

    @SerializedName("weekNum")
    val weekNum: Int,

    @SerializedName("yearNum")
    val yearNum: Int
)
