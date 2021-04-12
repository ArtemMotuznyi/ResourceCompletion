package com.github.artemmotuznyi.resourcecompletion.contributor

import com.github.artemmotuznyi.resourcecompletion.ResourceManager
import com.github.artemmotuznyi.resourcecompletion.SourceCodeType
import com.github.artemmotuznyi.resourcecompletion.provider.ResourceValueCompletionProvider
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.xml.XmlTokenType

class ResourceByValueCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            getPsiElementForStringResource(),
            ResourceValueCompletionProvider(
                ResourceManager(SourceCodeType.Xml.getStringsResourceTypes())
            )
        )

        extend(
            CompletionType.BASIC,
            getPsiElementForColorResource(),
            ResourceValueCompletionProvider(
                ResourceManager(SourceCodeType.Xml.getColorResourceTypes())
            )
        )
    }

    private fun getPsiElementForStringResource() =
        PlatformPatterns.psiElement(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN).withParent(
            XmlPatterns.xmlAttributeValue().withParent(
                XmlPatterns.xmlAttribute().withChild(XmlPatterns.psiElement(XmlTokenType.XML_NAME))
                    .withLocalName("text")
            )
        )

    private fun getPsiElementForColorResource() =
        PlatformPatterns.psiElement(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN).withParent(
            XmlPatterns.xmlAttributeValue().withParent(
                XmlPatterns.xmlAttribute().withChild(XmlPatterns.psiElement(XmlTokenType.XML_NAME))
                    .withLocalName("textColor")
            )
        )
}