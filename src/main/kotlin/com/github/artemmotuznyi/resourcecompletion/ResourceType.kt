package com.github.artemmotuznyi.resourcecompletion;

import org.jdom.Element
import java.lang.IllegalArgumentException


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
    },

    COLOR {
        override fun isElementValueValid(element: Element, inputValue: String): Boolean {
            val isColorElement = COLOR_MATCHER.toRegex().matches(inputValue)
            return isColorElement && element.text.contains(inputValue)
        }
    };

    abstract fun isElementValueValid(element: Element, inputValue: String): Boolean

    override fun toString(): String {
        return name.toLowerCase()
    }

    open fun getTag(): String = toString()

    companion object {
        private const val COLOR_MATCHER = "^#([a-fA-F0-9]{1,6})$"


        fun getTypeByElement(element: Element) = when (element.name) {
            STRING.toString() -> STRING
            PLURALS.toString() -> PLURALS
            COLOR.toString() -> COLOR
            else -> throw IllegalArgumentException() //TODO
        }

    }
}