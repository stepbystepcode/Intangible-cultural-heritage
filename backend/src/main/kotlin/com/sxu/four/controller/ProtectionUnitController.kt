package com.sxu.four.controller

import com.sxu.four.model.ProtectionUnit
import com.sxu.four.repository.ProtectionUnitRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/protection-units")
class ProtectionUnitController(val repository: ProtectionUnitRepository) {

    @GetMapping
    @Operation(summary = "Get all protection units", description = "Fetch a list of all protection units")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved all protection units"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    fun getAllUnits(): ResponseEntity<List<ProtectionUnit>> {
        val result = repository.findAll()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get protection unit by ID", description = "Fetch a specific protection unit by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved protection unit"),
        ApiResponse(responseCode = "404", description = "Protection unit not found")
    ])

    fun getUnitById(@PathVariable id: Int): ResponseEntity<ProtectionUnit> {
        val unit = repository.findById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(unit)
    }

    @PostMapping
    @Operation(summary = "Create a new protection unit", description = "Create a new protection unit and store it in the database")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Protection unit successfully created"),
        ApiResponse(responseCode = "400", description = "Bad request")
    ])
    fun createUnit(@RequestBody unit: ProtectionUnit): ResponseEntity<Map<String, Int>> {
        val newId = repository.create(unit)
        val body = mapOf("UnitID" to newId)
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing protection unit", description = "Update the details of an existing protection unit by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Protection unit successfully updated"),
        ApiResponse(responseCode = "404", description = "Protection unit not found")
    ])
    fun updateUnit(@PathVariable id: Int, @RequestBody unit: ProtectionUnit): ResponseEntity<Void> {
        val count = repository.update(id, unit)
        return if (count > 0) ResponseEntity.ok().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a protection unit", description = "Delete a protection unit by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Protection unit successfully deleted"),
        ApiResponse(responseCode = "404", description = "Protection unit not found")
    ])
    fun deleteUnit(@PathVariable id: Int): ResponseEntity<Void> {
        val count = repository.delete(id)
        return if (count > 0) ResponseEntity.noContent().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    // 获取该保护单位负责的所有项目
    @GetMapping("/{id}/projects")
    @Operation(summary = "Get projects managed by protection unit", description = "Fetch all projects managed by a given protection unit")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved projects managed by the protection unit"),
        ApiResponse(responseCode = "404", description = "Protection unit not found")
    ])
    fun getProjectsByUnit(@PathVariable id: Int): ResponseEntity<List<Map<String, Any>>> {
        val projects = repository.findProjectsByUnitId(id)
        return ResponseEntity.ok(projects)
    }
}