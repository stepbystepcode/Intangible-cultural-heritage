package com.sxu.four.model

import java.util.Date // 引入 Date 类型

data class NationalHeritageProject(
    //val projectId: Int? = null,
    val projectName: String,
    val category: String?,
    val announcementDate: Date?, // 修改为 Date 类型
    val type: String?,
    val applicationRegion: String?,
    val protectionUnitId: Int?,
    val latitude: Double?,
    val longitude: Double?,
    val detail:String?
)
