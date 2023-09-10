package com.project.fat.data.dto

typealias Fatman = ArrayList<FatmanElement>

data class FatmanElement (
    val name: String,
    val fatmanImageUrl: String,
    val fatmanId: Long
)
