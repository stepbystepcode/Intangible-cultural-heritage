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
}