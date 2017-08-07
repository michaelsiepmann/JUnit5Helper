package de.misi.idea.junit5.extensions

import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.junit.jupiter.api.Assertions.assertNotNull

data class CodeInsightTestFixtureWrapper(val fixture: CodeInsightTestFixture, val beforeSuffix: String, val afterSuffix: String) {

    fun executeIntentionTest(type: String, intention: String) {
        with(fixture) {
            configureByFile(type + beforeSuffix)
            val action = findSingleIntention(intention)
            assertNotNull(action)
            launchAction(action)
            checkResultByFile(type + afterSuffix)
        }
    }
}