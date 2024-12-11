package com.sxu.four

package com.sxu.four.services

import com.sxu.four.models.NationalHeritageProject
import com.sxu.four.repositories.ProjectRepository
import org.springframework.stereotype.Service

@Service
class ProjectService(private val projectRepository: ProjectRepository) {

    fun getAllProjects(): List<NationalHeritageProject> {
        return projectRepository.findAll().toList()
    }

    fun getProjectById(id: Int): NationalHeritageProject? {
        return projectRepository.findById(id).orElse(null)
    }

    fun saveProject(project: NationalHeritageProject): NationalHeritageProject {
        return projectRepository.save(project)
    }

    fun deleteProject(id: Int) {
        projectRepository.deleteById(id)
    }
}