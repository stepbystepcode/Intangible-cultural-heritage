package com.sxu.four.model

data class UnescoHeritage(
    val listId: Int? = null,
    val projectName: String,
    val yearIncluded: Int?,
    val type: String?,
    val applicationMethod: String?,
    val applicationCountry: String?
)