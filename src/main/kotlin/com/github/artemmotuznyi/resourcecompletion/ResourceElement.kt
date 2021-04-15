package com.github.artemmotuznyi.resourcecompletion

import com.intellij.codeInsight.lookup.LookupElement
import org.jdom.Element

class ResourceElement(
    private val element: Element,
    private val inputValue: String,
    private val completionPattern: String
) : LookupElement() {

    companion object {
        private const val ATTRIBUTE_KEY = "name"
    }

    private val type: ResourceType = ResourceType.getTypeByElementName(element.name)
    private val resourceKey: String = element.getAttributeValue(ATTRIBUTE_KEY)

    private val completionValue by lazy {
        String.format(
            completionPattern,
            type.tag,
            resourceKey
        )
    }
    private val allLookupStrings by lazy { sortedSetOf(inputValue, completionValue) }

    fun isResourceValueValid(): Boolean = type.isResourceValueValid(element, inputValue)

    override fun getLookupString(): String = completionValue

    override fun getAllLookupStrings(): MutableSet<String> = allLookupStrings

}