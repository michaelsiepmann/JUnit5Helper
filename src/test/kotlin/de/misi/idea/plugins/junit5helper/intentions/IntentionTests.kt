package de.misi.idea.plugins.junit5helper.intentions

import de.misi.idea.junit5.annotations.FixtureTestcase
import de.misi.idea.junit5.extensions.CodeInsightTestFixtureWrapper
import de.misi.idea.junit5.extensions.TestcaseRunner
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import java.io.File

private const val dataPath = "F:\\Daten\\IdeaProjects\\JUnit5Helper\\src\\test\\resources\\testData"

@FixtureTestcase(dataPath = dataPath, beforeSuffix = ".before.template.java", afterSuffix = ".after.template.java")
internal class IntentionTests {

    @TestFactory
    @DisplayName("Tests for add @Disabled to method")
    fun executeAddDisabledTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper) =
            createDynamicTests(testcaseRunner, fixture, "adddisabledmethod", ADD_DISABLED_TO_METHOD)

    @TestFactory
    @DisplayName("Tests for add @DisplayName")
    fun executeAddDisplayNameTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper) =
            createDynamicTests(testcaseRunner, fixture, "adddisplayname", ADD_DISPLAYNAME)

    @TestFactory
    @DisplayName("Tests for remove @Disabled from class")
    fun executeRemoveDisabledFromClassTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper) =
            createDynamicTests(testcaseRunner, fixture, "removedisabledclass", REMOVE_DISABLED_FROM_CLASS)

    @TestFactory
    @DisplayName("Test for remove @Disabled from method")
    fun executeRemoveDisabledFromMethodTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper) =
            createDynamicTests(testcaseRunner, fixture, "removedisabledmethod", REMOVE_DISABLED_FROM_METHOD)

    @TestFactory
    @DisplayName("Tests for surround with assertAll()")
    fun executeSurroundWithAssertAllTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper) =
            createDynamicTests(testcaseRunner, fixture, "surroundassertall", SURROUND_WITH_ASSERT_ALL_NAME)

    @TestFactory
    @DisplayName("Tests for surround with nested class")
    fun executeSurroundWithNestedTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper) =
            createDynamicTests(testcaseRunner, fixture, "surroundnested", SURROUND_WITH_NESTED_CLASS_NAME)

    private fun createDynamicTests(testcaseRunner: TestcaseRunner, fixture: CodeInsightTestFixtureWrapper, subpath: String, testname: String) =
            File(dataPath + File.separator + subpath)
                    .listFiles { _, name ->
                        name.contains(fixture.beforeSuffix)
                    }
                    .map {
                        it.name.substring(0, it.name.indexOf(fixture.beforeSuffix))
                    }
                    .map {
                        dynamicTest("Testing \"$it\"") {
                            testcaseRunner.run {
                                fixture.executeIntentionTest(subpath + File.separator + it, testname)
                            }
                        }
                    }
}