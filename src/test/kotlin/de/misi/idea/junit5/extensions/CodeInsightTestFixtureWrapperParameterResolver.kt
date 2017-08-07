package de.misi.idea.junit5.extensions

import com.intellij.testFramework.builders.JavaModuleFixtureBuilder
import com.intellij.testFramework.fixtures.*
import de.misi.idea.junit5.annotations.FixtureTestcase
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

class CodeInsightTestFixtureWrapperParameterResolver : ParameterResolver, AfterEachCallback {

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext) = parameterContext.isOfType(CodeInsightTestFixtureWrapper::class.java)

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any? {
        val annotation = extensionContext.getAnnotation(FixtureTestcase::class.java)
        if (annotation != null) {
            val testFixtureBuilder = IdeaTestFixtureFactory.getFixtureFactory().createFixtureBuilder(extensionContext.requiredTestMethod?.name ?: "unknown")
            val fixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(testFixtureBuilder.fixture)
            with(fixture) {
                testDataPath = annotation.dataPath
                val builder = testFixtureBuilder.addModule(JavaModuleFixtureBuilder::class.java)
                builder.addContentRoot(tempDirPath).addSourceRoot("")
                builder.setMockJdkLevel(JavaModuleFixtureBuilder.MockJdkLevel.jdk15)
                setUp()
            }
            extensionContext.store.put(STORE_KEY, fixture)
            return CodeInsightTestFixtureWrapper(fixture!!, annotation.beforeSuffix, annotation.afterSuffix)
        }
        return null
    }

    override fun afterEach(context: ExtensionContext) {
        val store = context.store
        (store.get(STORE_KEY) as CodeInsightTestFixture?)?.tearDown()
        store.remove(STORE_KEY)
    }

    companion object {

        const val STORE_KEY = "de.misi.idea.junit5.fixture"

    }
}