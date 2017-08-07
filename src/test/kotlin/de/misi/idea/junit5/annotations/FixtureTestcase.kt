package de.misi.idea.junit5.annotations

import de.misi.idea.junit5.extensions.CodeInsightTestFixtureWrapperParameterResolver
import de.misi.idea.junit5.extensions.TestcaseRunnerParameterResolver
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestcaseRunnerParameterResolver::class, CodeInsightTestFixtureWrapperParameterResolver::class)
annotation class FixtureTestcase(val dataPath: String, val beforeSuffix : String, val afterSuffix : String)