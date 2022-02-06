package de.misi.idea.plugins.junit5helper.surrounder

import com.intellij.lang.java.JavaLanguage
import com.intellij.lang.surroundWith.SurroundDescriptor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiStatement
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.util.parentOfType
import de.misi.idea.plugins.junit5helper.between
import de.misi.idea.plugins.junit5helper.intentions.hasTestAnnotation


class StatementSurroundDescriptor : SurroundDescriptor {

    override fun getElementsToSurround(file: PsiFile?, startOffset: Int, endOffset: Int): Array<PsiElement> {
        if (file == null) {
            return PsiElement.EMPTY_ARRAY
        }
        return findStatementsInRange(file, startOffset, endOffset)
    }

    override fun getSurrounders() = arrayOf(AssertAllSurrounder())

    override fun isExclusive() = false

    private fun findStatementsInRange(file: PsiFile, startOffset: Int, endOffset: Int): Array<PsiElement> {
        val firstStatement = elementAt(file, startOffset) ?: return PsiElement.EMPTY_ARRAY
        val lastStatement = elementAt(file, endOffset - 1) ?: return PsiElement.EMPTY_ARRAY
        val possibleStatements = firstStatement.parent
            .children
            .filterIsInstance<PsiStatement>()
            .between(firstStatement, lastStatement)
        return PsiUtilCore.toPsiElementArray(possibleStatements)
    }

    private fun elementAt(file: PsiFile, offset: Int): PsiStatement? {
        val result = file.viewProvider.findElementAt(offset, JavaLanguage.INSTANCE)
        val statement = result?.parentOfType<PsiStatement>(true) ?: return null
        val method = statement.parentOfType<PsiMethod>() ?: return null
        return if (method.modifierList.hasTestAnnotation()) {
            statement
        } else {
            null
        }
    }
}
