package com.sxu.four.controller

import com.sxu.four.model.ProtectionUnit
import com.sxu.four.repository.ProtectionUnitRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/protection-units")
class ProtectionUnitController(val repository: ProtectionUnitRepository) {

    @GetMapping
    fun getAllUnits(): ResponseEntity<List<ProtectionUnit>> {
        val result = repository.findAll()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun getUnitById(@PathVariable id: Int): ResponseEntity<ProtectionUnit> {
        val unit = repository.findById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(unit)
    }

    @PostMapping
    fun createUnit(@RequestBody unit: ProtectionUnit): ResponseEntity<Map<String, Int>> {
        val newId = repository.create(unit)
        val body = mapOf("UnitID" to newId)
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @PutMapping("/{id}")
    fun updateUnit(@PathVariable id: Int, @RequestBody unit: ProtectionUnit): ResponseEntity<Void> {
        val count = repository.update(id, unit)
        return if (count > 0) ResponseEntity.ok().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    fun deleteUnit(@PathVariable id: Int): ResponseEntity<Void> {
        val count = repository.delete(id)
        return if (count > 0) ResponseEntity.noContent().build() else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    // 获取该保护单位负责的所有项目
    @GetMapping("/{id}/projects")
    fun getProjectsByUnit(@PathVariable id: Int): ResponseEntity<List<Map<String, Any>>> {
        val projects = repository.findProjectsByUnitId(id)
        return ResponseEntity.ok(projects)
    }
}