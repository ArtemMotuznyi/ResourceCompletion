package com.github.artemmotuznyi.resourcecompletion

import org.jdom.Element
import java.lang.IllegalArgumentException

sealed class SourceCodeType(
    val pattern: String,
    private val availableResourceTypes: List<ResourceType>
) {

    fun isValidType(name: String?) = availableResourceTypes.any { it.name.equals(name, true) }

    class Xml(availableResourceTypes: List<ResourceType>) : SourceCodeType("@%s/%s", availableResourceTypes) {
        companion object {
            fun getStringsResourceTypes() = Xml(listOf(ResourceType.STRING))
        }
    }

    class JavaKotlin(availableResourceTypes: List<ResourceType>) : SourceCodeType("R.%s.%s", availableResourceTypes);

}

enum class ResourceType {

    STRING {
        override fun isElementValueValid(element: Element, inputValue: String) =
            element.text.contains(inputValue)
    },

    PLURALS {
        override fun isElementValueValid(element: Element, inputValue: String): Boolean {
            return element.content.any { it.value.contains(inputValue) }
        }

        override fun getTag(): String = STRING.toString()
    };

    abstract fun isElementValueValid(element: Element, inputValue: String): Boolean

    override fun toString(): String {
        return name.toLowerCase()
    }

    open fun getTag(): String = toString()

    companion object {

        fun getTypeByElement(element: Element) = when (element.name) {
            STRING.toString() -> STRING
            PLURALS.toString() -> PLURALS
            else -> throw IllegalArgumentException() //TODO
        }

    }
}