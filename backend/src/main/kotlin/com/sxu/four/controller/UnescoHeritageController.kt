package com.sxu.four.controller

import com.sxu.four.model.UnescoHeritage
import com.sxu.four.repository.UnescoHeritageRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/unesco-list")
class UnescoHeritageController(val repository: UnescoHeritageRepository) {

    @GetMapping
    fun getAllHeritage(): ResponseEntity<List<UnescoHeritage>> {
        val result = repository.findAll()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun getHeritageById(@PathVariable id: Int): ResponseEntity<UnescoHeritage> {
        val heritage = repository.findById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(heritage)
    }

    @PostMapping
    fun createHeritage(@RequestBody heritage: UnescoHeritage): ResponseEntity<Map<String, Int>> {
        val newId = repository.create(heritage)
        val body = mapOf("ListID" to newId)
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @PutMapping("/{id}")
    fun updateHeritage(@PathVariable id: Int, @RequestBody heritage: UnescoHeritage): ResponseEntity<Void> {
        val count = repository.update(id, heritage)
        return if (count > 0) ResponseEntity.ok().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    fun deleteHeritage(@PathVariable id: Int): ResponseEntity<Void> {
        val count = repository.delete(id)
        return if (count > 0) ResponseEntity.noContent().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }
}