package de.misi.idea.plugins.junit5helper.livetemplates

import com.intellij.codeInsight.template.Expression
import com.intellij.codeInsight.template.ExpressionContext
import com.intellij.codeInsight.template.Result
import com.intellij.codeInsight.template.TextResult
import org.junit.Test
import org.junit.Assert.assertEquals

class CamelCaseMethodNameMacroTest {

    @Test
    fun testCalculateResult() {
        assertEquals("halloDiesIstEinTest", CamelCaseMethodNameMacro().calculateResult(arrayOf(ExpressionStub("Hallo, dies ist ein Test.")), null).toString())
    }

    private class ExpressionStub(val result: Result?) : Expression() {

        constructor(text: String) : this(TextResult(text))

        override fun calculateQuickResult(context: ExpressionContext?) = result

        override fun calculateLookupItems(context: ExpressionContext?) = null

        override fun calculateResult(context: ExpressionContext?): Result? = result
    }
}