package de.misi.idea.junit5.extensions

import com.intellij.psi.codeStyle.CodeStyleSettingsManager.getSettings
import com.intellij.psi.codeStyle.JavaCodeStyleSettings
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory
import com.intellij.testFramework.fixtures.JavaTestFixtureFactory
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
                with(getSettings(project)) {
                    USE_FQ_CLASS_NAMES = true
                    USE_SINGLE_CLASS_IMPORTS = true
                    with(getCustomSettings(JavaCodeStyleSettings::class.java)) {
                        CLASS_NAMES_IN_JAVADOC = JavaCodeStyleSettings.FULLY_QUALIFY_NAMES_ALWAYS
                    }
                }
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