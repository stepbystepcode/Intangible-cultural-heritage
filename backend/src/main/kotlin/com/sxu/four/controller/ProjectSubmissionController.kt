package com.sxu.four.controller

import com.sxu.four.model.ProjectSubmission
import com.sxu.four.repository.ProjectSubmissionRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/submissions")
class ProjectSubmissionController(val repository: ProjectSubmissionRepository) {

    @GetMapping
    @Operation(summary = "Get all project submissions", description = "Fetch a list of all project submissions")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved all submissions"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    fun getAllSubmissions(): ResponseEntity<List<ProjectSubmission>> {
        val result = repository.findAll()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get submission by ID", description = "Fetch a specific project submission by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved submission"),
        ApiResponse(responseCode = "404", description = "Submission not found")
    ])
    fun getSubmissionById(@PathVariable id: Int): ResponseEntity<ProjectSubmission> {
        val submission = repository.findById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(submission)
    }

    @PostMapping
    @Operation(summary = "Create a new project submission", description = "Create a new submission for a project")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Submission successfully created"),
        ApiResponse(responseCode = "400", description = "Bad request")
    ])
    fun createSubmission(@RequestBody submission: ProjectSubmission): ResponseEntity<Map<String, Int>> {
        val newId = repository.create(submission)
        val body = mapOf("SubmissionID" to newId)
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project submission", description = "Update the details of an existing project submission")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Submission successfully updated"),
        ApiResponse(responseCode = "404", description = "Submission not found")
    ])
    fun updateSubmission(@PathVariable id: Int, @RequestBody submission: ProjectSubmission): ResponseEntity<Void> {
        val count = repository.update(id, submission)
        return if (count > 0) ResponseEntity.ok().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project submission", description = "Delete a project submission by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Submission successfully deleted"),
        ApiResponse(responseCode = "404", description = "Submission not found")
    ])
    fun deleteSubmission(@PathVariable id: Int): ResponseEntity<Void> {
        val count = repository.delete(id)
        return if (count > 0) ResponseEntity.noContent().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }
}