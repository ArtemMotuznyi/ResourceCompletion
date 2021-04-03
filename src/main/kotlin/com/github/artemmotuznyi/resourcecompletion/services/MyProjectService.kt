package com.github.artemmotuznyi.resourcecompletion.services

import com.github.artemmotuznyi.resourcecompletion.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
