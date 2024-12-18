package com.sxu.four.repository

import com.sxu.four.model.Inheritor
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet



@Repository
class InheritorRepository(val jdbcTemplate: JdbcTemplate) {

    private val rowMapper = RowMapper<Inheritor> { rs: ResultSet, _: Int ->
        Inheritor(
            inheritorId = rs.getInt("inheritorid"),
            name = rs.getString("name"),
            gender = rs.getString("gender"),
            ethnicity = rs.getString("ethnicity"),
            category = rs.getString("category"),
            projectId = rs.getInt("projectid").takeIf { !rs.wasNull() },
            latitude = rs.getDouble("latitude").takeIf { !rs.wasNull() },
            longitude = rs.getDouble("longitude").takeIf { !rs.wasNull() }
        )
    }

    fun findAll(): List<Inheritor> {
        val sql = """
            SELECT i.inheritorid, i.name, i.gender, i.ethnicity, i.category, i.projectid,
                   ST_Y(p.Location::geometry) AS latitude,
                   ST_X(p.Location::geometry) AS longitude
            FROM Inheritors i
            LEFT JOIN National_Heritage_Projects p ON i.projectid = p.projectid
        """.trimIndent()
        return jdbcTemplate.query(sql, rowMapper)
    }

    fun findById(id: Int): Inheritor? {
        val sql = """
            SELECT i.inheritorid, i.name, i.gender, i.ethnicity, i.category, i.projectid,
                   ST_Y(p.Location::geometry) AS latitude,
                   ST_X(p.Location::geometry) AS longitude
            FROM Inheritors i
            LEFT JOIN National_Heritage_Projects p ON i.projectid = p.projectid
            WHERE i.inheritorid = ?
        """.trimIndent()
        return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
    }

    fun create(inheritor: Inheritor): Int {
        // 插入时无需处理纬度经度，因为传承人本身没有location字段
        val sql = """
            INSERT INTO Inheritors (Name, Gender, Ethnicity, Category, ProjectID)
            VALUES (?, ?, ?, ?, ?) RETURNING InheritorID
        """.trimIndent()
        return jdbcTemplate.queryForObject(sql, Int::class.java,
            inheritor.name, inheritor.gender, inheritor.ethnicity, inheritor.category, inheritor.projectId)!!
    }

    fun update(id: Int, inheritor: Inheritor): Int {
        val sql = """
            UPDATE Inheritors SET Name = ?, Gender = ?, Ethnicity = ?, Category = ?, ProjectID = ?
            WHERE InheritorID = ?
        """.trimIndent()
        return jdbcTemplate.update(sql, inheritor.name, inheritor.gender, inheritor.ethnicity, inheritor.category, inheritor.projectId, id)
    }

    fun delete(id: Int): Int {
        val sql = "DELETE FROM Inheritors WHERE InheritorID = ?"
        return jdbcTemplate.update(sql, id)
    }

}


