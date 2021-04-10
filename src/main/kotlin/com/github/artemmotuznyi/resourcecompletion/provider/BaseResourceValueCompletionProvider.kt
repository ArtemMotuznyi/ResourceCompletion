package com.github.artemmotuznyi.resourcecompletion.provider

import com.github.artemmotuznyi.ResourceManager
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ProcessingContext
import org.jdom.Element
import org.jdom.JDOMException
import org.jetbrains.annotations.NotNull
import java.io.IOException

abstract class BaseResourceValueCompletionProvider(
    private val resourceManager: ResourceManager,
) : CompletionProvider<CompletionParameters>() {

    companion object {
        private const val XML_EXTENSION = ".xml"
        private const val VALUE_FOLDER = "app/src/main/res"
        private const val TAG_RESOURCES = "resources"
    }

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        parameters.editor.project?.let {
            val prefix = result.prefixMatcher.prefix
            if (prefix.isNotBlank()) {
                val files = getResourceFiles(it)
                if (files.isNotEmpty()) {
                    val resourceElements = getElementsFromResourceFiles(files)
                    if (resourceElements.isNotEmpty()) {
                        val completionElements = getCompletionResult(resourceElements, prefix)
                        result.addAllElements(completionElements)
                    }
                }
            }
        }
    }

    private fun getResourceFiles(project: Project): List<PsiFile> {
        val filenames = FilenameIndex.getAllFilenames(project)
        return filenames.filter {
            it.contains(XML_EXTENSION)
        }.flatMap {
            FilenameIndex.getFilesByName(project, it, GlobalSearchScope.projectScope(project)).toList()
        }.filter {
            it.virtualFile.path.contains(VALUE_FOLDER)
        }
    }

    private fun getElementsFromResourceFiles(files: List<PsiFile>): List<Element> {
        return try {
            files
                .map {
                    val root = JDOMUtil.load(it.text)
                    root.getChild(TAG_RESOURCES) ?: root
                }
                .filter { it.name == TAG_RESOURCES }
                .flatMap { it.children.orEmpty() }
        } catch (e: JDOMException) {
            emptyList()
        } catch (e: IOException) {
            emptyList()
        }
    }

    private fun getCompletionResult(elements: List<Element>, prefix: @NotNull String): List<LookupElement> {
        return resourceManager.generateResources(elements, prefix)
    }
}