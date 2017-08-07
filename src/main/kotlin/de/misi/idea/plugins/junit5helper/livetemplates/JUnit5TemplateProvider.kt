package de.misi.idea.plugins.junit5helper.livetemplates

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider

class JUnit5TemplateProvider : DefaultLiveTemplatesProvider {

    override fun getDefaultLiveTemplateFiles() = arrayOf("liveTemplates/JUnit5")

    override fun getHiddenLiveTemplateFiles() = emptyArray<String>()
}