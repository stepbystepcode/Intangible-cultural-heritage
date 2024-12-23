package com.sxu.four.repository

import com.sxu.four.model.Inheritor
import com.sxu.four.model.NationalHeritageProject
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class ProjectRepository(val jdbcTemplate: JdbcTemplate) {

    // RowMapper for NationalHeritageProject
    private val projectRowMapper = RowMapper<NationalHeritageProject> { rs: ResultSet, _: Int ->
        NationalHeritageProject(
            projectId = rs.getInt("projectid"),
            projectName = rs.getString("projectname"),
            category = rs.getString("category"),
            announcementDate = rs.getDate("announcementdate"),
            type = rs.getString("type"),
            applicationRegion = rs.getString("applicationregion"),
            protectionUnitId = rs.getInt("protectionunitid").takeIf { !rs.wasNull() },
            latitude = rs.getDouble("latitude").takeIf { !rs.wasNull() },
            longitude = rs.getDouble("longitude").takeIf { !rs.wasNull() },
            detail = rs.getString("detail")

        )
    }

    // RowMapper for Inheritor
    private val inheritorRowMapper = RowMapper<Inheritor> { rs: ResultSet, _: Int ->
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

    fun findAll(): List<NationalHeritageProject> {
        val sql = """
            SELECT projectid, projectname, category, announcementdate, type, applicationregion, protectionunitid,
                   ST_Y(location::geometry) AS latitude,
                   ST_X(location::geometry) AS longitude
            FROM national_heritage_projects
        """.trimIndent()
        return jdbcTemplate.query(sql, projectRowMapper)
    }

    fun findById(id: Int): NationalHeritageProject? {
        val sql = """
            SELECT projectid, projectname, category, announcementdate, type, applicationregion, protectionunitid,
                   ST_Y(location::geometry) AS latitude,
                   ST_X(location::geometry) AS longitude
            FROM national_heritage_projects
            WHERE projectid = ?
        """.trimIndent()
        return jdbcTemplate.query(sql, projectRowMapper, id).firstOrNull()
    }

    fun create(project: NationalHeritageProject): Int {
        // 如果需要同时设置坐标，请在INSERT时使用 ST_SetSRID(ST_MakePoint(longitude, latitude), 4326)
        // 若前端不传坐标，可在后端根据其他信息生成或留null
        // 假设这里project.latitude和project.longitude不为空时才设置坐标
        val sql = if (project.latitude != null && project.longitude != null) {
            """
            INSERT INTO national_heritage_projects (projectname, category, announcementdate, type, applicationregion, protectionunitid, location)
            VALUES (?, ?, ?, ?, ?, ?, ST_SetSRID(ST_MakePoint(?, ?), 4326)) RETURNING projectid
            """.trimIndent()
        } else {
            """
            INSERT INTO national_heritage_projects (projectname, category, announcementdate, type, applicationregion, protectionunitid)
            VALUES (?, ?, ?, ?, ?, ?) RETURNING projectid
            """.trimIndent()
        }

        return if (project.latitude != null && project.longitude != null) {
            jdbcTemplate.queryForObject(sql, Int::class.java,
                project.projectName, project.category, project.announcementDate, project.type, project.applicationRegion, project.protectionUnitId,
                project.longitude, project.latitude)!!
        } else {
            jdbcTemplate.queryForObject(sql, Int::class.java,
                project.projectName, project.category, project.announcementDate, project.type, project.applicationRegion, project.protectionUnitId)!!
        }
    }

    fun update(id: Int, project: NationalHeritageProject): Int {
        // 同理，如果要更新坐标，也在UPDATE中使用ST_SetSRID(...)
        val sql = if (project.latitude != null && project.longitude != null) {
            """
            UPDATE national_heritage_projects
            SET projectname = ?, category = ?, announcementdate = ?, type = ?, applicationregion = ?, protectionunitid = ?,
                location = ST_SetSRID(ST_MakePoint(?, ?), 4326)
            WHERE projectid = ?
            """.trimIndent()
        } else {
            """
            UPDATE national_heritage_projects
            SET projectname = ?, category = ?, announcementdate = ?, type = ?, applicationregion = ?, protectionunitid = ?
            WHERE projectid = ?
            """.trimIndent()
        }

        return if (project.latitude != null && project.longitude != null) {
            jdbcTemplate.update(sql,
                project.projectName, project.category, project.announcementDate, project.type, project.applicationRegion, project.protectionUnitId,
                project.longitude, project.latitude,
                id)
        } else {
            jdbcTemplate.update(sql,
                project.projectName, project.category, project.announcementDate, project.type, project.applicationRegion, project.protectionUnitId,
                id)
        }
    }

    fun delete(id: Int): Int {
        val sql = "DELETE FROM national_heritage_projects WHERE projectid = ?"
        return jdbcTemplate.update(sql, id)
    }

    fun search(category: String?, region: String?): List<NationalHeritageProject> {
        val sb = StringBuilder("""
            SELECT projectid, projectname, category, announcementdate, type, applicationregion, protectionunitid,
                   ST_Y(location::geometry) AS latitude,
                   ST_X(location::geometry) AS longitude
            FROM national_heritage_projects WHERE 1=1
        """.trimIndent())
        val params = mutableListOf<Any>()
        if (!category.isNullOrEmpty()) {
            sb.append(" AND category = ?")
            params.add(category)
        }
        if (!region.isNullOrEmpty()) {
            sb.append(" AND applicationregion = ?")
            params.add(region)
        }
        return jdbcTemplate.query(sb.toString(), projectRowMapper, *params.toTypedArray())
    }

    fun findInheritorsByProjectId(id: Int): List<Inheritor> {
        val sql = """
            SELECT i.InheritorID, i.Name, i.Gender, i.Ethnicity, i.Category, i.ProjectID,
                   ST_Y(p.Location::geometry) AS latitude,
                   ST_X(p.Location::geometry) AS longitude
            FROM Inheritors i
            JOIN national_heritage_projects p ON i.projectid = p.projectid
            WHERE i.projectid = ?
        """.trimIndent()
        return jdbcTemplate.query(sql, inheritorRowMapper, id)
    }

    //查询视图
    fun getProjectSummary(): List<Map<String, Any>> {
        val sql = "SELECT * FROM ProjectSummary"
        return jdbcTemplate.queryForList(sql)
    }

}