package com.sxu.four.controller

import com.sxu.four.model.Inheritor
import com.sxu.four.repository.InheritorRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/inheritors")
class InheritorController(val repository: InheritorRepository) {

    @GetMapping
    fun getAllInheritors(): ResponseEntity<List<Inheritor>> {
        val result = repository.findAll()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun getInheritorById(@PathVariable id: Int): ResponseEntity<Inheritor> {
        val result = repository.findById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(result)
    }

    @PostMapping
    fun createInheritor(@RequestBody inheritor: Inheritor): ResponseEntity<Map<String, Int>> {
        val newId = repository.create(inheritor)
        val body = mapOf("InheritorID" to newId)
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @PutMapping("/{id}")
    fun updateInheritor(@PathVariable id: Int, @RequestBody inheritor: Inheritor): ResponseEntity<Void> {
        val count = repository.update(id, inheritor)
        return if (count > 0) ResponseEntity.ok().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    fun deleteInheritor(@PathVariable id: Int): ResponseEntity<Void> {
        val count = repository.delete(id)
        return if (count > 0) ResponseEntity.noContent().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }
}