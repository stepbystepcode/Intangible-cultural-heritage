package com.sxu.four

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import javax.sql.DataSource

@RestController
class MessageController @Autowired constructor(private val dataSource: DataSource) {

    @GetMapping("/")
    fun getPostgresVersion(): String {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery("SELECT version();").use { resultSet ->
                    if (resultSet.next()) {
                        return resultSet.getString(1)
                    }
                }
            }
        }
        return "Unknown PostgreSQL Version"
    }
    @GetMapping("/api/getIch")
    fun getIch(): ResponseEntity<Map<String, Any>> {
        return try {
            dataSource.connection.use { connection ->
                connection.createStatement().use { statement ->
                    statement.executeQuery("""
                    SELECT 
                        ProjectID, 
                        ProjectName, 
                        Category, 
                        AnnouncementDate, 
                        Type, 
                        ApplicationRegion, 
                        ProtectionUnitID, 
                        ST_AsText(Location) AS Location
                    FROM National_Heritage_Projects
                """).use { resultSet ->
                        val results = mutableListOf<Map<String, Any>>()
                        while (resultSet.next()) {
                            // 解析 Location 字段
                            val location = resultSet.getString("Location")
                            val coordinates = location
                                ?.removePrefix("POINT(")
                                ?.removeSuffix(")")
                                ?.split(" ")
                                ?.map { it.toDouble() }

                            val latitude = coordinates?.getOrNull(1) // 纬度
                            val longitude = coordinates?.getOrNull(0) // 经度

                            val row = mapOf(
                                "projectID" to resultSet.getInt("ProjectID"),
                                "projectName" to resultSet.getString("ProjectName"),
                                "category" to resultSet.getString("Category"),
                                "announcementDate" to resultSet.getDate("AnnouncementDate"),
                                "type" to resultSet.getString("Type"),
                                "applicationRegion" to resultSet.getString("ApplicationRegion"),
                                "protectionUnitID" to resultSet.getInt("ProtectionUnitID"),
                                "location" to mapOf( // 将经纬度封装为对象
                                    "latitude" to latitude,
                                    "longitude" to longitude
                                )
                            )
                            results.add(row)
                        }
                        ResponseEntity.ok(mapOf("status" to "success", "data" to results))
                    }
                }
            }
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("status" to "error", "message" to (e.message ?: "Unknown error occurred")))
        }
    }
}
