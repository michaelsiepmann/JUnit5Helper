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

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement) =
            findStatements(editor?.caretModel?.currentCaret, element).isNotEmpty()

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val factory = JavaPsiFacade.getInstance(project).elementFactory
        val statements = findStatements(editor?.caretModel?.currentCaret, element)
        if (statements.isNotEmpty()) {
            val topParent = statements[0].parent as PsiCodeBlock
            val innerText = parseSelectedText(statements)
            var statement = factory.createStatementFromText("assertAll(\n$innerText\n);", null) as PsiExpressionStatement
            statement = CodeStyleManager.getInstance(project).reformat(statement) as PsiExpressionStatement
            topParent.statements[topParent.statements.indexOf(statements[0])].replace(statement)
            statements.forEach {
                val index = topParent.statements.indexOf(it)
                if (index >= 0) {
                    topParent.statements[index].delete()
                }
            }
        }
    }

    private fun findStatements(currentCaret: Caret?, element: PsiElement): List<PsiExpressionStatement> {
        if (currentCaret != null && currentCaret.selectedText?.isNotBlank() == true) {
            return currentCaret.collectStatements(element.containingFile)
        }
        val statement = element.getParentOfType(PsiExpressionStatement::class.java)
        return if (statement != null && statement.isAssertionCall()) {
            listOf(statement)
        } else {
            emptyList()
        }
    }

    private fun parseSelectedText(statements: Collection<PsiExpressionStatement>) =
            statements.joinToString(",\n") {
                it.text.replace(Regex("^(.*);$"), "() -> $1")
            }

    private fun PsiExpressionStatement.isAssertionCall() = text.trim().startsWith("assert")

    private fun Caret.collectStatements(file: PsiFile): List<PsiExpressionStatement> {
        var start = selectionStart
        val end = selectionEnd
        val result = mutableListOf<PsiExpressionStatement>()
        while (start < end) {
            val item = file.findElementAt(start)
            val (statement, size) = findExpressionStatement(item)
            if (statement == null || !statement.isAssertionCall()) {
                return emptyList()
            }
            result.add(statement)
            start += size
        }
        return result
    }

    private fun findExpressionStatement(item: PsiElement?): Pair<PsiExpressionStatement?, Int> {
        val statement = item?.getParentOfType(PsiExpressionStatement::class.java)
        if (statement != null || item !is PsiWhiteSpace) {
            return Pair(statement, statement?.textLength ?: 0)
        }
        val sibling = PsiTreeUtil.getNextSiblingOfType(item, PsiExpressionStatement::class.java)
        return Pair(sibling, calculateSize(item, sibling))
    }

    private fun calculateSize(item: PsiElement?, sibling: PsiElement?): Int =
            when {
                item == null -> 0
                sibling == null || sibling == item -> item.textLength
                else -> item.textLength + calculateSize(item.nextSibling, sibling)
            }
}