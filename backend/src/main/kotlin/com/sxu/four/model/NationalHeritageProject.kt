package com.sxu.four.model

data class NationalHeritageProject(
    val projectId: Int? = null,
    val projectName: String,
    val category: String?,
    val announcementDate: String?,
    val type: String?,
    val applicationRegion: String?,
    val protectionUnitId: Int?,
    val latitude: Double?,
    val longitude: Double?
)