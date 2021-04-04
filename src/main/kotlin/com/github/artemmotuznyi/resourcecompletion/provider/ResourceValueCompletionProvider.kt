package com.github.artemmotuznyi.resourcecompletion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.JDOMUtil
import com.intellij.psi.PsiFile
import org.jdom.Element
import org.jdom.JDOMException
import java.io.IOException

//TODO rename
abstract class ResourceValueCompletionProvider(
    private val resourceType: String
) : CompletionProvider<CompletionParameters>() {

    companion object {
        private const val TAG_RESOURCES = "resources"
        private const val TAG_NAME = "name"
    }

    protected fun getElementsFromResourceFiles(files: List<PsiFile>): List<Element> {
        return try {
            files.asSequence()
                .map {
                    val root = JDOMUtil.load(it.text)
                    root.getChild(TAG_RESOURCES) ?: root
                }
                .filter { it.name == TAG_RESOURCES }
                .flatMap { it.children.orEmpty().asSequence() }
                .filter { it.name == resourceType }
                .toList()
        } catch (e: JDOMException) {
            e.printStackTrace()
            emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    protected fun getCompletionResult(elements: List<Element>): List<LookupElement> {
        return elements.map {
            val key: String = it.getAttributeValue(TAG_NAME)
            LookupElementBuilder.create("@${it.name}/${key}").withLookupString(it.text)
        }
    }
}