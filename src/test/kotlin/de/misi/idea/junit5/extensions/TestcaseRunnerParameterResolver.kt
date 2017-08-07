package de.misi.idea.junit5.extensions

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

class TestcaseRunnerParameterResolver : ParameterResolver {

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext) = parameterContext.isOfType(TestcaseRunner::class.java)

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext) = TestcaseRunner()
}