package de.misi.idea.plugins.junit5helper.livetemplates

import com.intellij.codeInsight.template.Expression
import com.intellij.codeInsight.template.ExpressionContext
import com.intellij.codeInsight.template.Result
import com.intellij.codeInsight.template.TextResult
import com.intellij.codeInsight.template.macro.MacroBase
import de.misi.idea.plugins.junit5helper.lowerFirstChar
import de.misi.idea.plugins.junit5helper.removeAccents

class CamelCaseMethodNameMacro : MacroBase("camelCaseMethodName", "Converts a string to a method name.") {
    override fun calculateResult(params: Array<out Expression>, context: ExpressionContext?, quick: Boolean): Result? {
        val text = getTextResult(params, context)
        if (text != null) {
            return TextResult(convertTextToMethodName(text))
        }
        return null
    }

    private fun convertTextToMethodName(text: String): String {
        var markUpper = false
        return text.toCharArray()
            .joinToString("") {
                if (it.isLetterOrDigit()) {
                    if (markUpper) {
                        markUpper = false
                        it.uppercaseChar().toString()
                    } else {
                        it.toString()
                    }
                } else {
                    markUpper = true
                    ""
                }
            }
            .removeAccents()
            .lowerFirstChar()
    }
}