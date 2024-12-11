package com.sxu.four.model

data class ProjectSubmission(
    val submissionId: Int? = null,
    val projectId: Int,
    val listId: Int,
    val applicationCountry: String?
)