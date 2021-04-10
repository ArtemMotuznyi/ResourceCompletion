package com.github.artemmotuznyi

import org.jdom.Element

enum class SourceCodeType(val pattern: String) {

    XML("@%s/%s"),
    OTHER("R.%s.%s");

}

enum class ResourceType {
    STRING {
        override fun isElementValueValid(element: Element, inputValue: String) =
            element.text.contains(inputValue, true)
    },

    UNKNOWN {
        override fun isElementValueValid(element: Element, inputValue: String) = false
    };

    override fun toString(): String {
        return name.toLowerCase()
    }

    abstract fun isElementValueValid(element: Element, inputValue: String): Boolean

    companion object {

        fun getTypeByElement(element: Element) = when (element.name) {
            STRING.toString() -> STRING
            else -> UNKNOWN
        }

    }
}