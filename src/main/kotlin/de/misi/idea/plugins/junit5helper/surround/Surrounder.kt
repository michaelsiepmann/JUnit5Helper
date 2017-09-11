package de.misi.idea.plugins.junit5helper.surround

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.util.PsiTreeUtil.getNextSiblingOfType
import de.misi.idea.plugins.junit5helper.intentions.addAnnotation
import de.misi.idea.plugins.junit5helper.intentions.getParentOfType
import de.misi.idea.plugins.junit5helper.upperFirstChar

internal fun surroundWithAssertAll(project: Project, editor: Editor?, element: PsiElement) {
    val factory = JavaPsiFacade.getInstance(project).elementFactory
    val statements = findStatements(editor?.caretModel?.currentCaret, element)
    if (statements.isNotEmpty()) {
        val topParent = statements[0].parent as PsiCodeBlock
        val innerText = parseSelectedText(statements)
        var statement = factory.createStatementFromText("org.junit.jupiter.api.Assertions.assertAll(\n$innerText\n);", null) as PsiExpressionStatement
        statement = project.shortenAndReformat(statement)
        topParent.statements[topParent.statements.indexOf(statements[0])].replace(statement)
        statements.forEach {
            val index = topParent.statements.indexOf(it)
            if (index >= 0) {
                topParent.statements[index].delete()
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T : PsiElement> Project.shortenAndReformat(statement: T): T {
    val result = JavaCodeStyleManager.getInstance(this).shortenClassReferences(statement) as T
    return CodeStyleManager.getInstance(this).reformat(result) as T
}

internal fun findStatements(currentCaret: Caret?, element: PsiElement): List<PsiExpressionStatement> {
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
            it.text.replace(Regex("^\\s*"), "() -> ").replace(Regex("\\);$"), ")")
        }

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
    val sibling = getNextSiblingOfType(item, PsiExpressionStatement::class.java)
    return Pair(sibling, calculateSize(item, sibling))
}

private fun calculateSize(item: PsiElement?, sibling: PsiElement?): Int =
        when {
            item == null -> 0
            sibling == null || sibling == item -> item.textLength
            else -> item.textLength + calculateSize(item.nextSibling, sibling)
        }

private fun PsiExpressionStatement.isAssertionCall() = text.trim().startsWith("assert")

internal fun surroundWithNestedClass(element: PsiElement, project: Project) {
    val method = element.getParentOfType(PsiMethod::class.java)
    if (method != null) {
        val parentClazz = method.getParentOfType(PsiClass::class.java)
        if (parentClazz != null) {
            val factory = JavaPsiFacade.getInstance(project).elementFactory
            val clazz = factory.createClass(method.name.upperFirstChar() + "Test")
            clazz.addAnnotation("@org.junit.jupiter.api.Nested", factory, clazz)
            clazz.addAnnotation("@org.junit.jupiter.api.DisplayName(\"\")", factory, clazz)
            clazz.modifierList?.setModifierProperty("public", false)
            clazz.add(method)
            val index = parentClazz.children.indexOf(method)
            parentClazz.children[index].delete()
            parentClazz.addBefore(clazz, parentClazz.children[index])
            CodeStyleManager.getInstance(project).reformat(clazz)
        }
    }
}
