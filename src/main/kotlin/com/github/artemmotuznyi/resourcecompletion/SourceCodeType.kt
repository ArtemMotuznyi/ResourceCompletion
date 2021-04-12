package com.github.artemmotuznyi.resourcecompletion

sealed class SourceCodeType(
    val pattern: String,
    private val availableResourceTypes: List<ResourceType>
) {

    fun isValidType(name: String?) = availableResourceTypes.any { it.name.equals(name, true) }

    class Xml(availableResourceTypes: List<ResourceType>) : SourceCodeType("@%s/%s", availableResourceTypes) {
        companion object {
            fun getStringsResourceTypes() = Xml(listOf(ResourceType.STRING))
            fun getColorResourceTypes() = Xml(listOf(ResourceType.COLOR))
        }
    }

    class JavaKotlin(availableResourceTypes: List<ResourceType>) : SourceCodeType("R.%s.%s", availableResourceTypes);

}