package de.misi.idea.junit5.extensions

import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.junit.jupiter.api.Assertions.assertNotNull

data class CodeInsightTestFixtureWrapper(val fixture: CodeInsightTestFixture, val beforeSuffix: String, val afterSuffix: String) {

    fun executeIntentionTest(type: String, intention: String) {
        fixture.configureByFile(type + beforeSuffix)
        val action = fixture.findSingleIntention(intention)
        assertNotNull(action)
        fixture.launchAction(action)
        fixture.checkResultByFile(type + afterSuffix)
    }
}