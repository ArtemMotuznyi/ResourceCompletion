package com.github.artemmotuznyi.resourcecompletion.provider

import com.github.artemmotuznyi.resourcecompletion.ResourceElement
import com.github.artemmotuznyi.resourcecompletion.ResourceType
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ProcessingContext
import com.intellij.util.containers.filterSmart
import org.jdom.Element
import org.jdom.JDOMException
import java.io.File
import java.io.IOException

class ResourceValueCompletionProvider(
    private val completionPattern: String,
    private val availableTypes: List<ResourceType>
) : CompletionProvider<CompletionParameters>() {

    companion object {
        private const val XML_EXTENSION = "xml"
        private const val VALUE_FOLDER = "app/src/main/res/values"
        private const val TAG_RESOURCES = "resources"
    }

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        parameters.editor.project?.let { project ->
            result.prefixMatcher.prefix
                .takeIf { it.isNotEmpty() }
                ?.let { prefix ->
                    val files = getResourceFilesByFolder(project)
                    val resourceElements = getElementsFromResourceFiles(files)
                    val completions = generateResourcesCompletion(resourceElements, prefix)
                    result.addAllElements(completions)
                }
        }
    }

    private fun getResourceFilesByFolder(project: Project): List<PsiFile> {
        val files = File(project.basePath, VALUE_FOLDER).listFiles().orEmpty()
        return files.filter { XML_EXTENSION.contains(it.extension) }.flatMap {
            FilenameIndex.getFilesByName(project, it.name, GlobalSearchScope.projectScope(project)).toList()
        }
    }

    private fun getElementsFromResourceFiles(files: List<PsiFile>): List<Element> {
        return try {
            files.map {
                val root = JDOMUtil.load(it.text)
                root.getChild(TAG_RESOURCES) ?: root
            }.filter {
                it.name == TAG_RESOURCES
            }.flatMap {
                it.children.orEmpty()
            }
        } catch (e: JDOMException) {
            println("getElementsFromResourceFiles: Something went wrong $e")
            emptyList()
        } catch (e: IOException) {
            println("getElementsFromResourceFiles: Something went wrong $e")
            emptyList()
        }
    }

    private fun generateResourcesCompletion(elements: List<Element>, inputValue: String): List<ResourceElement> =
        filterElementsByType(elements)
            .map { ResourceElement(it, inputValue, completionPattern) }
            .filter { it.isResourceValueValid() }
            .distinctBy { it.lookupString }

    private fun filterElementsByType(elements: List<Element>) =
        elements.filterSmart { availableTypes.any { type -> type.toString() == it.name } }
}
