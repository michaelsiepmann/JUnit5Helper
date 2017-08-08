package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiTreeUtil

class SurroundWithAssertAllIntention : PsiElementBaseIntentionAction(), IntentionAction {
    override fun getText() = familyName

    override fun getFamilyName() = SURROUND_WITH_ASSERT_ALL_NAME

    override fun startInWriteAction() = true

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return findStatements(editor?.caretModel?.currentCaret, element).isNotEmpty()
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val factory = JavaPsiFacade.getInstance(project).elementFactory
        val statements = findStatements(editor?.caretModel?.currentCaret, element)
        if (statements.isNotEmpty()) {
            val topParent = statements[0].parent as PsiCodeBlock
            val (innerText, indices) = parseSelectedText(statements, topParent)
            var statement = factory.createStatementFromText("assertAll(\n$innerText\n);", null) as PsiExpressionStatement
            statement = CodeStyleManager.getInstance(project).reformat(statement) as PsiExpressionStatement
            topParent.statements[indices[0]].replace(statement)
            indices.drop(1).forEach {
                topParent.statements[it].delete()
            }
        }
    }

    private fun findStatements(currentCaret: Caret?, element: PsiElement): List<PsiExpressionStatement> {
        if (currentCaret != null && currentCaret.selectedText?.isNotBlank() ?: false) {
            return currentCaret.collectStatements(element.containingFile)
        }
        val statement = element.getParentOfType(PsiExpressionStatement::class.java)
        return if (statement != null && statement.isAssertionCall()) {
            listOf(statement)
        } else {
            emptyList()
        }
    }

    private fun parseSelectedText(statements: Collection<PsiExpressionStatement>, topParent: PsiCodeBlock): Pair<String, List<Int>> {
        return Pair(
                statements
                        .map {
                            it.text.replace(Regex("(.*);"), "() -> $1")
                        }
                        .joinToString(",\n"),
                createIndices(statements, topParent)
        )
    }

    private fun createIndices(statements: Collection<PsiExpressionStatement>, topParent: PsiCodeBlock): List<Int> {
        return statements.map {
            topParent.statements.indexOf(it)
        }
    }

    private fun PsiExpressionStatement.isAssertionCall(): Boolean {
        return this.text.trim().startsWith("assert")
    }

    private fun Caret.collectStatements(file: PsiFile): List<PsiExpressionStatement> {
        var start = selectionStart
        val end = selectionEnd
        val result = mutableListOf<PsiExpressionStatement>()
        while (start < end) {
            val item = file.findElementAt(start)
            val statement = findExpressionStatement(item)
            if (statement == null || !statement.isAssertionCall()) {
                return emptyList()
            }
            result.add(statement)
            start += statement.textLength
        }
        return result
    }

    private fun findExpressionStatement(item: PsiElement?): PsiExpressionStatement? {
        val statement = item?.getParentOfType(PsiExpressionStatement::class.java)
        if (statement != null || item !is PsiWhiteSpace) {
            return statement
        }
        return PsiTreeUtil.getNextSiblingOfType(item, PsiExpressionStatement::class.java)
    }
}