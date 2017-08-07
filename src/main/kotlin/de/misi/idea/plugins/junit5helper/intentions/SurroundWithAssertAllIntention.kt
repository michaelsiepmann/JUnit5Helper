package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.codeStyle.CodeStyleManager

class SurroundWithAssertAllIntention : PsiElementBaseIntentionAction(), IntentionAction {
    override fun getText() = familyName

    override fun getFamilyName() = SURROUND_WITH_ASSERT_ALL_NAME

    override fun startInWriteAction() = true

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val text = editor?.caretModel?.currentCaret?.selectedText
        if (text?.trim()?.startsWith("assert") == true) {
            return true
        }
        return element.hasParentOfType(PsiExpressionStatement::class.java)
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val factory = JavaPsiFacade.getInstance(project).elementFactory
        val parent = element.getParentOfType(PsiExpressionStatement::class.java)
        if (parent != null) {
            val topParent = parent.parent as PsiCodeBlock
            val (innerText, indices) = parseSelectedText(editor, parent, topParent)
            var statement = factory.createStatementFromText("assertAll(\n$innerText\n);", null) as PsiExpressionStatement
            statement = CodeStyleManager.getInstance(project).reformat(statement) as PsiExpressionStatement
            topParent.statements[indices[0]].replace(statement)
            indices.drop(1).forEach {
                topParent.statements[it].delete()
            }
        }
    }

    private fun parseSelectedText(editor: Editor?, parent: PsiExpressionStatement, topParent: PsiCodeBlock): Pair<String, List<Int>> {
        val text = editor?.caretModel?.currentCaret?.selectedText
        if (text != null && text.trim().startsWith("assert")) {
            return Pair(
                    text.split("\n")
                            .map {
                                it.trim().replace(Regex("(.*);"), "() -> $1")
                            }
                            .joinToString(",\n"),
                    createIndices(text, topParent)
            )
        }
        return Pair("() -> " + parent.text.replace(";", ""), listOf(topParent.statements.indexOf(parent)))
    }

    private fun createIndices(text: String, topParent: PsiCodeBlock): List<Int> {
        val result = mutableListOf<Int>()
        text.split("\n")
                .forEach { line ->
                    val element = topParent.statements.firstOrNull {
                        it.text == line.trim()
                    }
                    if (element != null) {
                        result.add(topParent.statements.indexOf(element))
                    }
                }
        return result
    }
}