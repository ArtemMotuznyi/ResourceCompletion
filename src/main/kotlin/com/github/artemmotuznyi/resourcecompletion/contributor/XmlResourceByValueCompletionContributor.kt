package com.github.artemmotuznyi.resourcecompletion.contributor

import com.github.artemmotuznyi.resourcecompletion.ResourceType
import com.github.artemmotuznyi.resourcecompletion.provider.ResourceValueCompletionProvider
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.StandardPatterns
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.xml.XmlTokenType

class XmlResourceByValueCompletionContributor : CompletionContributor() {

    companion object {
        private const val PATTERN = "@%s/%s"
    }

    init {
        extend(ResourceType.STRING)
        extend(ResourceType.COLOR)
    }

    private fun extend(type: ResourceType) {
        extend(
            CompletionType.BASIC,
            generateCompletionPlaceByResourceTypes(type),
            ResourceValueCompletionProvider(PATTERN, listOf(type))
        )
    }

    private fun generateCompletionPlaceByResourceTypes(type: ResourceType) =
        PlatformPatterns.psiElement(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN).withParent(
            XmlPatterns.xmlAttributeValue().withParent(
                XmlPatterns.xmlAttribute().withChild(XmlPatterns.psiElement(XmlTokenType.XML_NAME))
                    .withLocalName(StandardPatterns.string().oneOf(type.availableXmlAttributes))
            )
        )
}
