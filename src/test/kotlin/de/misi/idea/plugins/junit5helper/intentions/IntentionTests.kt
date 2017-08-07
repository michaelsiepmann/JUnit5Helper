package de.misi.idea.plugins.junit5helper.intentions

import de.misi.idea.junit5.annotations.FixtureTestcase
import de.misi.idea.junit5.extensions.CodeInsightTestFixtureWrapper
import de.misi.idea.junit5.extensions.TestcaseRunner
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import java.io.File

private const val dataPath = "F:\\Daten\\IdeaProjects\\JUnit5Helper\\src\\test\\resources\\testData"

@FixtureTestcase(dataPath = dataPath, beforeSuffix = ".before.template.java", afterSuffix = ".after.template.java")
internal class IntentionTests {

    @TestFactory
    fun executeAddDisabledTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper) =
            createDynamicTests(testcaseRunner, fixture, "adddisabled", ADD_DISABLED)

    @TestFactory
    fun executeAddDisplayNameTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper) =
            createDynamicTests(testcaseRunner, fixture, "adddisplayname", ADD_DISPLAYNAME)

    @TestFactory
    fun executeRemoveDisabledFromClassTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper) =
            createDynamicTests(testcaseRunner, fixture, "removedisabledclass", REMOVE_DISABLED_FROM_CLASS)

    @TestFactory
    fun executeRemoveDisabledFromMethodTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper) =
            createDynamicTests(testcaseRunner, fixture, "removedisabledmethod", REMOVE_DISABLED_FROM_METHOD)

    @TestFactory
    fun executeSurroundWithAssertAllTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper) =
            createDynamicTests(testcaseRunner, fixture, "surroundassertall", SURROUND_WITH_ASSERT_ALL_NAME)

    @TestFactory
    fun executeSurroundWithNestedTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper) =
            createDynamicTests(testcaseRunner, fixture, "surroundnested", SURROUND_WITH_NESTED_CLASS_NAME)

    private fun createDynamicTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper, path: String, testname: String) =
            File(dataPath + File.separator + path)
                    .listFiles { _, name ->
                        name.contains(fixture.beforeSuffix)
                    }
                    .map {
                        it.name.substring(0, it.name.indexOf(fixture.beforeSuffix))
                    }
                    .map {
                        dynamicTest("Testing \"$it\"") {
                            testcaseRunner.run {
                                fixture.executeIntentionTest(path + File.separator + it, testname)
                            }
                        }
                    }
}