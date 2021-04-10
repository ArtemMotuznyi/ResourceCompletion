package com.github.artemmotuznyi

import com.intellij.codeInsight.lookup.LookupElement
import org.jdom.Element

class Resource(
    private val element: Element,
    private val inputValue: String,
    sourceCodeType: SourceCodeType
) : LookupElement() {

    companion object {
        private const val ATTRIBUTE_KEY = "name"
    }

    val type: ResourceType = ResourceType.getTypeByElement(element)

    private val resourceKey: String = element.getAttributeValue(ATTRIBUTE_KEY)

    private val completionValue by lazy {
        String.format(
            sourceCodeType.pattern,
            type.toString(),
            resourceKey
        )
    }
    private val allLookupStrings by lazy { sortedSetOf(inputValue, completionValue) }

    fun isResourceValueValid(): Boolean = type.isElementValueValid(element, inputValue)

    override fun getLookupString(): String = completionValue

    override fun getAllLookupStrings(): MutableSet<String> = allLookupStrings

}

abstract class ResourceManager(
    protected val resourceTypes: List<ResourceType>,
    protected val codeType: SourceCodeType
) {

    abstract fun generateResources(
        elements: List<Element>,
        inputValue: String
    ): List<Resource>

}


class StringsResourceManager(
    codeType: SourceCodeType
) : ResourceManager(listOf(ResourceType.STRING, ResourceType.PLURALS), codeType) {


    override fun generateResources(
        elements: List<Element>,
        inputValue: String
    ): List<Resource> =

        elements.filter {
            resourceTypes.any { type -> type.toString().equals(it.name, true) }
        }.map {
            Resource(it, inputValue, codeType)
        }.filter {
            it.isResourceValueValid()
        }

}