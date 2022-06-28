package de.misi.idea.plugins.junit5helper

import com.intellij.DynamicBundle

class JUnit5HelperBundle: DynamicBundle("messages.JUnit5Bundle") {

    companion object {
        val INSTANCE = JUnit5HelperBundle()
    }
}