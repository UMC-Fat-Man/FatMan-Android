package com.project.fat.data.store

data class StoreAvata(
    val id : Long,
    val fatmanImage : String,
    val activated : Boolean,
    val fatmanName : String,
    var selected : Boolean
    )