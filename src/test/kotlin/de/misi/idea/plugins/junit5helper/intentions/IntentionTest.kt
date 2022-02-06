package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import org.junit.Before
import org.junit.Test
import java.io.File

class IntentionTest : LightJavaCodeInsightFixtureTestCase() {

    @Before
    override fun setUp() {
        super.setUp()
    }

    override fun getTestDataPath(): String {
        return "src/test/resources/testData"
    }

    @Test
    fun testAddDisabledToMethodIntention() {
        doTests("AddDisabledToMethodIntention", ADD_DISABLED_TO_METHOD)
    }

    @Test
    fun testAddDisplayNameToMethodIntention() {
        doTests("AddDisplayNameToMethodIntention", ADD_DISPLAYNAME)
    }

    @Test
    fun testRemoveDisabledFromClassIntention() {
        doTests("RemoveDisabledFromClassIntention", REMOVE_DISABLED_FROM_CLASS)
    }

    @Test
    fun testRemoveDisabledFromMethodIntention() {
        doTests("RemoveDisabledFromMethodIntention", REMOVE_DISABLED_FROM_METHOD)
    }

    private fun doTests(path: String, hint: String) {
        val files = File(testDataPath + "/$path")
            .listFiles { _, name -> name.contains(".before.") }
            .map { it.name.substringBefore(".before") }
        if (files.isEmpty()) {
            fail("No tests found below $testDataPath/$path")
        }
        files.forEach {
            doTest(path, it, hint)
        }
    }

    private fun doTest(path: String, testName: String, hint: String) {
        myFixture.configureByFile("$path/$testName.before.template.java")
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("$path/$testName.after.template.java")
    }
}