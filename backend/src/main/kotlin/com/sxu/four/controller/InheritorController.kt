package com.sxu.four.controller

import com.sxu.four.model.Inheritor
import com.sxu.four.repository.InheritorRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/inheritors")
class InheritorController(val repository: InheritorRepository) {

    @GetMapping
    @Operation(summary = "Get all inheritors", description = "Fetch all inheritors from the database")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved all inheritors"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    fun getAllInheritors(): ResponseEntity<List<Inheritor>> {
        val result = repository.findAll()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inheritor by ID", description = "Fetch inheritor by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved inheritor"),
        ApiResponse(responseCode = "404", description = "Inheritor not found")
    ])
    fun getInheritorById(@PathVariable id: Int): ResponseEntity<Inheritor> {
        val result = repository.findById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(result)
    }

    @PostMapping
    @Operation(summary = "Create a new inheritor", description = "Create a new inheritor and store it in the database")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Inheritor successfully created"),
        ApiResponse(responseCode = "400", description = "Bad request")
    ])
    fun createInheritor(@RequestBody inheritor: Inheritor): ResponseEntity<Map<String, Int>> {
        val newId = repository.create(inheritor)
        val body = mapOf("InheritorID" to newId)
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing inheritor", description = "Update an inheritor by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Inheritor successfully updated"),
        ApiResponse(responseCode = "404", description = "Inheritor not found")
    ])
    fun updateInheritor(@PathVariable id: Int, @RequestBody inheritor: Inheritor): ResponseEntity<Void> {
        val count = repository.update(id, inheritor)
        return if (count > 0) ResponseEntity.ok().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an inheritor", description = "Delete an inheritor by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Inheritor successfully deleted"),
        ApiResponse(responseCode = "404", description = "Inheritor not found")
    ])
    fun deleteInheritor(@PathVariable id: Int): ResponseEntity<Void> {
        val count = repository.delete(id)
        return if (count > 0) ResponseEntity.noContent().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }
}