package com.sxu.four.repository

import com.sxu.four.model.ProtectionUnit
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class ProtectionUnitRepository(val jdbcTemplate: JdbcTemplate) {

    private val rowMapper = RowMapper<ProtectionUnit> { rs: ResultSet, _: Int ->
        ProtectionUnit(
            unitId = rs.getInt("unitid"),
            unitName = rs.getString("unitname"),
            region = rs.getString("region"),
            contactInfo = rs.getString("contactinfo")
        )
    }

    fun findAll(): List<ProtectionUnit> {
        val sql = "SELECT * FROM Protection_Units"
        return jdbcTemplate.query(sql, rowMapper)
    }

    fun findById(id: Int): ProtectionUnit? {
        val sql = "SELECT * FROM Protection_Units WHERE UnitID = ?"
        return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
    }

    fun create(unit: ProtectionUnit): Int {
        val sql = """
            INSERT INTO Protection_Units (UnitName, Region, ContactInfo)
            VALUES (?, ?, ?) RETURNING UnitID
        """.trimIndent()
        return jdbcTemplate.queryForObject(sql, Int::class.java, unit.unitName, unit.region, unit.contactInfo)!!
    }

    fun update(id: Int, unit: ProtectionUnit): Int {
        val sql = """
            UPDATE Protection_Units SET UnitName = ?, Region = ?, ContactInfo = ? 
            WHERE UnitID = ?
        """.trimIndent()
        return jdbcTemplate.update(sql, unit.unitName, unit.region, unit.contactInfo, id)
    }

    fun delete(id: Int): Int {
        val sql = "DELETE FROM Protection_Units WHERE UnitID = ?"
        return jdbcTemplate.update(sql, id)
    }

    // 获取该保护单位负责的所有项目
    fun findProjectsByUnitId(id: Int): List<Map<String, Any>> {
        val sql = """
            SELECT p.* FROM National_Heritage_Projects p
            WHERE p.ProtectionUnitID = ?
        """.trimIndent()
        return jdbcTemplate.queryForList(sql, id)
    }
}