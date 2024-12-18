package com.sxu.four.controller

import com.sxu.four.model.UnescoHeritage
import com.sxu.four.repository.UnescoHeritageRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/unesco-list")
class UnescoHeritageController(val repository: UnescoHeritageRepository) {

    @GetMapping
    @Operation(summary = "Get all UNESCO heritage sites", description = "Fetch a list of all UNESCO heritage sites")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved all UNESCO heritage sites"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    fun getAllHeritage(): ResponseEntity<List<UnescoHeritage>> {
        val result = repository.findAll()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get UNESCO heritage site by ID", description = "Fetch a specific UNESCO heritage site by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved UNESCO heritage site"),
        ApiResponse(responseCode = "404", description = "UNESCO heritage site not found")

    ])
    fun getHeritageById(@PathVariable id: Int): ResponseEntity<UnescoHeritage> {
        val heritage = repository.findById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(heritage)
    }

    @PostMapping
    @Operation(summary = "Create a new UNESCO heritage site", description = "Create a new UNESCO heritage site and store it in the database")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "UNESCO heritage site successfully created"),
        ApiResponse(responseCode = "400", description = "Bad request")
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    fun createHeritage(@RequestBody heritage: UnescoHeritage): ResponseEntity<Map<String, Int>> {
        val newId = repository.create(heritage)
        val body = mapOf("ListID" to newId)
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing UNESCO heritage site", description = "Update the details of an existing UNESCO heritage site by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "UNESCO heritage site successfully updated"),
        ApiResponse(responseCode = "404", description = "UNESCO heritage site not found")
    ])
    fun updateHeritage(@PathVariable id: Int, @RequestBody heritage: UnescoHeritage): ResponseEntity<Void> {
        val count = repository.update(id, heritage)
        return if (count > 0) ResponseEntity.ok().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a UNESCO heritage site", description = "Delete a UNESCO heritage site by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "UNESCO heritage site successfully deleted"),
        ApiResponse(responseCode = "404", description = "UNESCO heritage site not found")
    ])
    fun deleteHeritage(@PathVariable id: Int): ResponseEntity<Void> {
        val count = repository.delete(id)
        return if (count > 0) ResponseEntity.noContent().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }
}