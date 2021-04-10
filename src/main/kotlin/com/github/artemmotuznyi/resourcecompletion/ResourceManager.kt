package com.github.artemmotuznyi.resourcecompletion

import com.intellij.util.containers.filterSmart
import org.jdom.Element

class ResourceManager(private val codeType: SourceCodeType) {

    fun generateResourcesCompletion(
        elements: List<Element>,
        inputValue: String
    ): List<ResourceElement> = filterElementsByType(elements).map {
        ResourceElement(it, inputValue, codeType)
    }.filter { it.isResourceValueValid() }.distinctBy { it.lookupString }

    private fun filterElementsByType(elements: List<Element>) = elements.filterSmart { codeType.isValidType(it.name) }
}