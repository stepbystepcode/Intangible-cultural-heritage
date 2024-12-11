package com.sxu.four.model

data class Inheritor(
    val inheritorId: Int? = null,
    val name: String,
    val gender: String?,
    val ethnicity: String?,
    val category: String?,
    val projectId: Int?,
    val latitude: Double?,
    val longitude: Double?
)