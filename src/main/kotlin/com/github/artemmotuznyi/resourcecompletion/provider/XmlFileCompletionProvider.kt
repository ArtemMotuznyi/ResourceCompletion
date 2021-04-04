package com.github.artemmotuznyi.resourcecompletion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ProcessingContext

abstract class XmlFileCompletionProvider(
    private val resourceType: String
) : ResourceValueCompletionProvider(resourceType) {

    companion object {
        private const val XML = ".xml"
        private const val VALUE_FOLDER = "app/src/main/res"
    }

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        parameters.editor.project?.let {
            val prefix = result.prefixMatcher.prefix
            if (prefix.isNotBlank()) {
                val files = xmlFiles(it)
                val elements = getElementsFromResourceFiles(files)
                val completionElements = getCompletionResult(elements)
                result.addAllElements(completionElements)
            }
        }
    }


    private fun xmlFiles(project: Project): List<PsiFile> {
        val filenames = FilenameIndex.getAllFilenames(project)
        return filenames.filter {
            it.contains(XML)
        }.flatMap {
            FilenameIndex.getFilesByName(project, it, GlobalSearchScope.projectScope(project)).toList()
        }.filter {
            it.virtualFile.path.contains(VALUE_FOLDER) && it.name.contains(resourceType)
        }
    }
}