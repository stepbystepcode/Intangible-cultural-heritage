package com.sxu.four.controller
import org.springframework.jdbc.core.RowMapper
import com.sxu.four.model.Inheritor
import com.sxu.four.model.NationalHeritageProject
import com.sxu.four.repository.ProjectRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*



@RestController
@RequestMapping("/api/projects")
class ProjectController(val repository: ProjectRepository) {
    val rowMapper = RowMapper<NationalHeritageProject> { rs, _ ->
        NationalHeritageProject(
            //projectId = rs.getInt("projectid"),
            projectName = rs.getString("projectname"),
            category = rs.getString("category"),
            announcementDate = rs.getDate("announcementdate"),
            type = rs.getString("type"),
            applicationRegion = rs.getString("applicationregion"),
            protectionUnitId = rs.getInt("protectionunitid"),
            latitude = rs.getDouble("latitude"),
            longitude = rs.getDouble("longitude"),
            detail=rs.getString("detail")
        )
    }

    @GetMapping
    @Operation(summary = "Get all national heritage projects", description = "Fetch a list of all national heritage projects")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved all projects"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    fun getAllProjects(): ResponseEntity<List<NationalHeritageProject>> {
        val result = repository.findAll()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "input ID then get project")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Project successfully created"),
        ApiResponse(responseCode = "400", description = "Bad request")
    ])
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
    @Operation(summary = "Update an existing national heritage project", description = "Update the details of an existing project by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Project successfully updated"),
        ApiResponse(responseCode = "404", description = "Project not found")
    ])
    fun updateProject(@PathVariable id: Int, @RequestBody project: NationalHeritageProject): ResponseEntity<Void> {
        val count = repository.update(id, project)
        return if (count > 0) ResponseEntity.ok().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a national heritage project", description = "Delete a project by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Project successfully deleted"),
        ApiResponse(responseCode = "404", description = "Project not found")
    ])
    fun deleteProject(@PathVariable id: Int): ResponseEntity<Void> {
        val count = repository.delete(id)
        return if (count > 0) ResponseEntity.noContent().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    // 搜索项目
    @GetMapping("/search")
    @Operation(summary = "Search projects", description = "Search for national heritage projects based on category or region")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved search results"),
        ApiResponse(responseCode = "400", description = "Bad request")
    ])
    fun searchProjects(@RequestParam(required = false) category: String?,
                       @RequestParam(required = false) region: String?): ResponseEntity<List<NationalHeritageProject>> {
        val result = repository.search(category, region)
        return ResponseEntity.ok(result)
    }

    // 获取某项目的传承人
    @GetMapping("/{id}/inheritors")
    @Operation(summary = "Get inheritors by project ID", description = "Fetch the inheritors associated with a given project by project ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved inheritors"),
        ApiResponse(responseCode = "404", description = "Project not found")
    ])
    fun getInheritorsByProject(@PathVariable id: Int): ResponseEntity<List<Inheritor>> {
        val result = repository.findInheritorsByProjectId(id)
        return ResponseEntity.ok(result)
    }

//    //触发器添加日志
//    @GetMapping("/logs")
//    fun getNationalHeritageLogs(): ResponseEntity<List<Map<String, Any>>> {
//        val logs = logRepository.getLogsByTable("national_heritage_projects")
//        return ResponseEntity.ok(logs)
//
//    }

    //获取视图
    @GetMapping("/summary")
    fun getProjectSummary(): ResponseEntity<List<Map<String, Any>>> {
        val result = repository.getProjectSummary()
        return ResponseEntity.ok(result)
    }

}