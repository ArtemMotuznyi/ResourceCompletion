package com.github.artemmotuznyi.resourcecompletion;

import org.jdom.Element
import java.lang.IllegalArgumentException


enum class ResourceType {


    STRING {
        override val availableXmlAttributes: List<String>
            get() = listOf("text", "hint", "contentDescription")

        override fun isResourceValueValid(element: Element, inputValue: String) = element.text.contains(inputValue)
    },

    PLURALS {
        override fun isResourceValueValid(element: Element, inputValue: String): Boolean =
            element.content.any { it.value.contains(inputValue) }
    },

    COLOR {
        override val availableXmlAttributes: List<String>
            get() = listOf("textColor", "textColorHint", "background")

        private val colorPattern = "^#([a-fA-F0-9]{1,6})$"

        override fun isResourceValueValid(element: Element, inputValue: String): Boolean {
            val isColorElement = colorPattern.toRegex().matches(inputValue)
            return isColorElement && element.text.contains(inputValue, true)
        }
    },

    STRING_ARRAY {
        override val tag: String
            get() = "array"

        override fun isResourceValueValid(element: Element, inputValue: String): Boolean = false
        override fun toString(): String = super.toString().replace("_", "-")
    };

    open val tag: String
        get() = toString()
    open val availableXmlAttributes: List<String>
        get() = emptyList()

    abstract fun isResourceValueValid(element: Element, inputValue: String): Boolean
    override fun toString(): String = name.toLowerCase()

    companion object {

        fun getTypeByElementName(elementName: String) = when (elementName) {
            STRING.toString() -> STRING
            PLURALS.toString() -> PLURALS
            COLOR.toString() -> COLOR
            STRING_ARRAY.toString() -> STRING_ARRAY
            else -> throw IllegalArgumentException() //TODO
        }

    }
}