package com.sxu.four.controller

import com.sxu.four.model.ProjectSubmission
import com.sxu.four.repository.ProjectSubmissionRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/submissions")
class ProjectSubmissionController(val repository: ProjectSubmissionRepository) {

    @GetMapping
    fun getAllSubmissions(): ResponseEntity<List<ProjectSubmission>> {
        val result = repository.findAll()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun getSubmissionById(@PathVariable id: Int): ResponseEntity<ProjectSubmission> {
        val submission = repository.findById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(submission)
    }

    @PostMapping
    fun createSubmission(@RequestBody submission: ProjectSubmission): ResponseEntity<Map<String, Int>> {
        val newId = repository.create(submission)
        val body = mapOf("SubmissionID" to newId)
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @PutMapping("/{id}")
    fun updateSubmission(@PathVariable id: Int, @RequestBody submission: ProjectSubmission): ResponseEntity<Void> {
        val count = repository.update(id, submission)
        return if (count > 0) ResponseEntity.ok().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    fun deleteSubmission(@PathVariable id: Int): ResponseEntity<Void> {
        val count = repository.delete(id)
        return if (count > 0) ResponseEntity.noContent().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }
}