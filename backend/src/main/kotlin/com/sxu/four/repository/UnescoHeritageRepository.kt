package com.sxu.four.repository

import com.sxu.four.model.UnescoHeritage
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class UnescoHeritageRepository(val jdbcTemplate: JdbcTemplate) {

    private val rowMapper = RowMapper<UnescoHeritage> { rs: ResultSet, _: Int ->
        UnescoHeritage(
            listId = rs.getInt("listid"),
            projectName = rs.getString("projectname"),
            yearIncluded = rs.getInt("yearincluded"),
            type = rs.getString("type"),
            applicationMethod = rs.getString("applicationmethod"),
            applicationCountry = rs.getString("applicationcountry")
        )
    }

    fun findAll(): List<UnescoHeritage> {
        val sql = "SELECT * FROM UNESCO_Heritage_List"
        return jdbcTemplate.query(sql, rowMapper)
    }

    fun findById(id: Int): UnescoHeritage? {
        val sql = "SELECT * FROM UNESCO_Heritage_List WHERE ListID = ?"
        return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
    }

    fun create(heritage: UnescoHeritage): Int {
        val sql = """
            INSERT INTO UNESCO_Heritage_List (ProjectName, YearIncluded, Type, ApplicationMethod, ApplicationCountry)
            VALUES (?, ?, ?, ?, ?) RETURNING ListID
        """.trimIndent()
        return jdbcTemplate.queryForObject(sql, Int::class.java,
            heritage.projectName, heritage.yearIncluded, heritage.type, heritage.applicationMethod, heritage.applicationCountry)!!
    }

    fun update(id: Int, heritage: UnescoHeritage): Int {
        val sql = """
            UPDATE UNESCO_Heritage_List 
            SET ProjectName = ?, YearIncluded = ?, Type = ?, ApplicationMethod = ?, ApplicationCountry = ?
            WHERE ListID = ?
        """.trimIndent()
        return jdbcTemplate.update(sql, heritage.projectName, heritage.yearIncluded, heritage.type, heritage.applicationMethod, heritage.applicationCountry, id)
    }

    fun delete(id: Int): Int {
        val sql = "DELETE FROM UNESCO_Heritage_List WHERE ListID = ?"
        return jdbcTemplate.update(sql, id)
    }
}