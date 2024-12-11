package com.sxu.four.repository

import com.sxu.four.model.ProjectSubmission
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class ProjectSubmissionRepository(val jdbcTemplate: JdbcTemplate) {

    private val rowMapper = RowMapper<ProjectSubmission> { rs: ResultSet, _: Int ->
        ProjectSubmission(
            submissionId = rs.getInt("submissionid"),
            projectId = rs.getInt("projectid"),
            listId = rs.getInt("listid"),
            applicationCountry = rs.getString("applicationcountry")
        )
    }

    fun findAll(): List<ProjectSubmission> {
        val sql = "SELECT * FROM Project_Submissions"
        return jdbcTemplate.query(sql, rowMapper)
    }

    fun findById(id: Int): ProjectSubmission? {
        val sql = "SELECT * FROM Project_Submissions WHERE SubmissionID = ?"
        return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
    }

    fun create(submission: ProjectSubmission): Int {
        val sql = """
            INSERT INTO Project_Submissions (ProjectID, ListID, ApplicationCountry)
            VALUES (?, ?, ?) RETURNING SubmissionID
        """.trimIndent()
        return jdbcTemplate.queryForObject(sql, Int::class.java, submission.projectId, submission.listId, submission.applicationCountry)!!
    }

    fun update(id: Int, submission: ProjectSubmission): Int {
        val sql = """
            UPDATE Project_Submissions
            SET ProjectID = ?, ListID = ?, ApplicationCountry = ?
            WHERE SubmissionID = ?
        """.trimIndent()
        return jdbcTemplate.update(sql, submission.projectId, submission.listId, submission.applicationCountry, id)
    }

    fun delete(id: Int): Int {
        val sql = "DELETE FROM Project_Submissions WHERE SubmissionID = ?"
        return jdbcTemplate.update(sql, id)
    }
}