package com.github.artemmotuznyi.resourcecompletion

import com.github.artemmotuznyi.resourcecompletion.provider.StringResourcesForXmlProvider
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.StandardPatterns
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.xml.XmlTokenType


class ResourceCompletionContributor : CompletionContributor() {

    init {
        extend(CompletionType.BASIC, getPsiElementForStringResource(), StringResourcesForXmlProvider())
    }

    private fun getPsiElementForStringResource() =
        PlatformPatterns.psiElement(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN).withParent(
            XmlPatterns.xmlAttributeValue().withParent(
                XmlPatterns.xmlAttribute().withChild(XmlPatterns.psiElement(XmlTokenType.XML_NAME))
                    .withLocalName("text")
            )
        )
}

