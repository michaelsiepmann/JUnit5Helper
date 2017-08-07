package de.misi.idea.junit5.extensions

import com.intellij.testFramework.builders.JavaModuleFixtureBuilder
import com.intellij.testFramework.fixtures.*
import de.misi.idea.junit5.annotations.FixtureTestcase
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

class CodeInsightTestFixtureWrapperParameterResolver : ParameterResolver, AfterEachCallback {

    private var fixture: CodeInsightTestFixture? = null

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext) = parameterContext.isOfType(CodeInsightTestFixtureWrapper::class.java)

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any? {
        val annotation = extensionContext.getAnnotation(FixtureTestcase::class.java)
        if (annotation != null) {
            val testFixtureBuilder = IdeaTestFixtureFactory.getFixtureFactory().createFixtureBuilder(extensionContext.requiredTestMethod?.name ?: "unknown")
            fixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(testFixtureBuilder.fixture)
            fixture?.initialize(testFixtureBuilder, annotation.dataPath)
            return CodeInsightTestFixtureWrapper(fixture!!, annotation.beforeSuffix, annotation.afterSuffix)
        }
        return null
    }

    private fun CodeInsightTestFixture.initialize(testFixtureBuilder: TestFixtureBuilder<IdeaProjectTestFixture>, path: String) {
        testDataPath = path
        val builder = testFixtureBuilder.addModule(JavaModuleFixtureBuilder::class.java)
        builder.addContentRoot(tempDirPath).addSourceRoot("")
        builder.setMockJdkLevel(JavaModuleFixtureBuilder.MockJdkLevel.jdk15)
        setUp()
    }

    override fun afterEach(context: ExtensionContext?) {
        fixture?.tearDown()
        fixture = null
    }
}