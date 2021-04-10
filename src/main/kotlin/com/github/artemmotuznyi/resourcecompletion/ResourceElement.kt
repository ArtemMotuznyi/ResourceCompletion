package com.github.artemmotuznyi.resourcecompletion

import com.intellij.codeInsight.lookup.LookupElement
import org.jdom.Element

class ResourceElement(
    private val element: Element,
    private val inputValue: String,
    sourceCodeType: SourceCodeType
) : LookupElement() {

    companion object {
        private const val ATTRIBUTE_KEY = "name"
    }

    private val type: ResourceType = ResourceType.getTypeByElement(element)
    private val resourceKey: String = element.getAttributeValue(ATTRIBUTE_KEY)

    private val completionValue by lazy {
        String.format(
            sourceCodeType.pattern,
            type.getTag(),
            resourceKey
        )
    }
    private val allLookupStrings by lazy { sortedSetOf(inputValue, completionValue) }

    fun isResourceValueValid(): Boolean = type.isElementValueValid(element, inputValue)

    override fun getLookupString(): String = completionValue

    override fun getAllLookupStrings(): MutableSet<String> = allLookupStrings

}