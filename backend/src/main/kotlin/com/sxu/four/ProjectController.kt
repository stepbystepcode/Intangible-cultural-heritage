package com.sxu.four.controllers

import com.sxu.four.models.NationalHeritageProject
import com.sxu.four.services.HeritageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/projects")
class ProjectController(private val heritageService: HeritageService) {

    // 获取所有项目
    @GetMapping
    fun getAllProjects(): ResponseEntity<List<NationalHeritageProject>> {
        return ResponseEntity.ok(heritageService.getAllProjects())
    }

    // 根据ID获取项目详情
    @GetMapping("/{id}")
    fun getProjectById(@PathVariable id: Int): ResponseEntity<NationalHeritageProject> {
        val project = heritageService.getProjectById(id)
        return project?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    // 创建新项目
    @PostMapping
    fun createProject(@RequestBody project: NationalHeritageProject): ResponseEntity<NationalHeritageProject> {
        val savedProject = heritageService.saveProject(project)
        return ResponseEntity.ok(savedProject)
    }

    // 更新项目
    @PutMapping("/{id}")
    fun updateProject(
        @PathVariable id: Int,
        @RequestBody project: NationalHeritageProject
    ): ResponseEntity<NationalHeritageProject> {
        val existingProject = heritageService.getProjectById(id)
        return if (existingProject != null) {
            val updatedProject = heritageService.saveProject(project.copy(projectId = id))
            ResponseEntity.ok(updatedProject)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // 删除项目
    @DeleteMapping("/{id}")
    fun deleteProject(@PathVariable id: Int): ResponseEntity<String> {
        val project = heritageService.getProjectById(id)
        return if (project != null) {
            heritageService.deleteProject(id)
            ResponseEntity.ok("Project deleted successfully")
        } else {
            ResponseEntity.notFound().build()
        }
    }
}