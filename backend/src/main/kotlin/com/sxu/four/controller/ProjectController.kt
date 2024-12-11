package com.sxu.four.controller
import org.springframework.jdbc.core.RowMapper
import com.sxu.four.model.Inheritor
import com.sxu.four.model.NationalHeritageProject
import com.sxu.four.repository.ProjectRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/projects")
class ProjectController(val repository: ProjectRepository) {
    val rowMapper = RowMapper<NationalHeritageProject> { rs, _ ->
        NationalHeritageProject(
            projectId = rs.getInt("projectid"),
            projectName = rs.getString("projectname"),
            category = rs.getString("category"),
            announcementDate = rs.getString("announcementdate"),
            type = rs.getString("type"),
            applicationRegion = rs.getString("applicationregion"),
            protectionUnitId = rs.getInt("protectionunitid"),
            latitude = rs.getDouble("latitude"),
            longitude = rs.getDouble("longitude")
        )
    }

    @GetMapping
    fun getAllProjects(): ResponseEntity<List<NationalHeritageProject>> {
        val result = repository.findAll()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun getProjectById(@PathVariable id: Int): ResponseEntity<NationalHeritageProject> {
        val proj = repository.findById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(proj)
    }

    @PostMapping
    fun createProject(@RequestBody project: NationalHeritageProject): ResponseEntity<Map<String, Int>> {
        val newId = repository.create(project)
        val body = mapOf("ProjectID" to newId)
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @PutMapping("/{id}")
    fun updateProject(@PathVariable id: Int, @RequestBody project: NationalHeritageProject): ResponseEntity<Void> {
        val count = repository.update(id, project)
        return if (count > 0) ResponseEntity.ok().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    fun deleteProject(@PathVariable id: Int): ResponseEntity<Void> {
        val count = repository.delete(id)
        return if (count > 0) ResponseEntity.noContent().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    // 搜索项目
    @GetMapping("/search")
    fun searchProjects(@RequestParam(required = false) category: String?,
                       @RequestParam(required = false) region: String?): ResponseEntity<List<NationalHeritageProject>> {
        val result = repository.search(category, region)
        return ResponseEntity.ok(result)
    }

    // 获取某项目的传承人
    @GetMapping("/{id}/inheritors")
    fun getInheritorsByProject(@PathVariable id: Int): ResponseEntity<List<Inheritor>> {
        val result = repository.findInheritorsByProjectId(id)
        return ResponseEntity.ok(result)
    }
}