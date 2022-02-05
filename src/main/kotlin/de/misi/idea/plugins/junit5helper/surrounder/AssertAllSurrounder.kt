package de.misi.idea.plugins.junit5helper.surrounder

import com.intellij.lang.surroundWith.Surrounder
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import de.misi.idea.plugins.junit5helper.shortenAndReformat

class AssertAllSurrounder : Surrounder {
    override fun getTemplateDescription() = "assertAll"

    override fun isApplicable(elements: Array<out PsiElement>) = true

    override fun surroundElements(project: Project, editor: Editor, elements: Array<out PsiElement>): TextRange? {
        val factory = JavaPsiFacade.getInstance(project).elementFactory
        val statements = elements.mapNotNull { getParentOfType(it, PsiExpressionStatement::class.java, false) }
        if (statements.isNotEmpty()) {
            val topParent = statements[0].parent as PsiCodeBlock
            val innerText = parseSelectedText(statements)
            var statement = factory.createStatementFromText(
                "org.junit.jupiter.api.Assertions.assertAll(\n$innerText\n);",
                null
            ) as PsiExpressionStatement
            statement = project.shortenAndReformat(statement)
            topParent.statements[topParent.statements.indexOf(statements[0])].replace(statement)
            statements.forEach {
                val index = topParent.statements.indexOf(it)
                if (index >= 0) {
                    topParent.statements[index].delete()
                }
            }
            return TextRange(statement.textRange.endOffset, statement.textRange.endOffset)
        }
        return null
    }

    private fun parseSelectedText(statements: Collection<PsiExpressionStatement>) =
        statements.joinToString(",\n") {
            it.text.replace(Regex("^\\s*"), "() -> ").replace(Regex("\\);$"), ")")
        }
}