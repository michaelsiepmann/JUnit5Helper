package de.misi.idea.junit5.annotations

import de.misi.idea.junit5.extensions.CodeInsightTestFixtureParameterResolver
import de.misi.idea.junit5.extensions.TestcaseRunnerParameterResolver
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestcaseRunnerParameterResolver::class, CodeInsightTestFixtureParameterResolver::class)
annotation class FixtureTestcase(val dataPath: String, val beforeSuffix : String, val afterSuffix : String)