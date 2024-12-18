package com.sxu.four.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class LogRepository(val jdbcTemplate: JdbcTemplate) {
    fun getLogsByTable(tableName: String): List<Map<String, Any>> {
        val sql = "SELECT * FROM Heritage_Log WHERE table_name = ? ORDER BY action_time DESC"
        return jdbcTemplate.queryForList(sql, tableName)
    }
}

