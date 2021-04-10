package com.github.artemmotuznyi.resourcecompletion.provider

import com.github.artemmotuznyi.SourceCodeType
import com.github.artemmotuznyi.StringsResourceManager

class StringResourcesForXmlProvider(sourceCodeType: SourceCodeType) : BaseResourceValueCompletionProvider(
    StringsResourceManager(sourceCodeType)
)