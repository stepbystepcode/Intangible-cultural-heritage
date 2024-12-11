package com.sxu.four

package com.sxu.four.repositories

import com.sxu.four.models.NationalHeritageProject
import org.springframework.data.repository.CrudRepository

interface ProjectRepository : CrudRepository<NationalHeritageProject, Int>