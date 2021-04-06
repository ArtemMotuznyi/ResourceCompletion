package com.github.artemmotuznyi.resourcecompletion.provider

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
    private val resourceType: List<String>,
) : CompletionProvider<CompletionParameters>() {

    companion object {
        private const val XML_EXTENSION = ".xml"
        private const val VALUE_FOLDER = "app/src/main/res"
        private const val TAG_RESOURCES = "resources"
        private const val TAG_NAME = "name"
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
        val filenames = FilenameIndex.getAllFilenames(project).asSequence()
        return filenames.filter {
            it.contains(XML_EXTENSION) && resourceType.any { type -> it.contains(type, true) }
        }.flatMap {
            FilenameIndex.getFilesByName(project, it, GlobalSearchScope.projectScope(project)).asSequence()
        }.filter {
            it.virtualFile.path.contains(VALUE_FOLDER)
        }.toList()
    }

    private fun getElementsFromResourceFiles(files: List<PsiFile>): List<Element> {
        return try {
            files.asSequence()
                .map {
                    val root = JDOMUtil.load(it.text)
                    root.getChild(TAG_RESOURCES) ?: root
                }
                .filter { it.name == TAG_RESOURCES }
                .flatMap { it.children.orEmpty().asSequence() }
                .filter { resourceType.any { type -> it.name.contains(type, true) } }
                .toList()
        } catch (e: JDOMException) {
            e.printStackTrace()
            emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun getCompletionResult(elements: List<Element>, prefix: @NotNull String): List<LookupElement> {
        return elements.filter {
            it.text.contains(prefix, true)
        }.map {
            val key: String = it.getAttributeValue(TAG_NAME)
            LookupElementBuilder.create("@${it.name}/${key}").withLookupString(prefix)
        }
    }
}