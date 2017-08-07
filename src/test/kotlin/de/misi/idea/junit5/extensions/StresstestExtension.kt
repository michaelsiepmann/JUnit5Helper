package de.misi.idea.junit5.extensions

import com.intellij.openapi.application.impl.ApplicationInfoImpl
import com.intellij.openapi.util.Disposer
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class StresstestExtension : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        ApplicationInfoImpl.setInStressTest(true)
        // turn off Disposer debugging for performance tests
        Disposer.setDebugMode(false)
    }

    override fun afterEach(context: ExtensionContext?) {
        Disposer.setDebugMode(true)
    }
}