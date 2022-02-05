package de.misi.idea.plugins.junit5helper.surrounder

import com.intellij.lang.java.JavaLanguage
import com.intellij.lang.surroundWith.SurroundDescriptor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElement.EMPTY_ARRAY
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilCore
import de.misi.idea.plugins.junit5helper.between
import de.misi.idea.plugins.junit5helper.intentions.hasTestAnnotation

class MethodSurroundDescriptor : SurroundDescriptor {

    override fun getElementsToSurround(file: PsiFile?, startOffset: Int, endOffset: Int): Array<PsiElement> {
        if (file == null) {
            return EMPTY_ARRAY
        }
        return findMethodsInRange(file, startOffset, endOffset)
    }

    override fun getSurrounders() = arrayOf(NestedClassSurrounder())

    override fun isExclusive() = false

    private fun findMethodsInRange(file: PsiFile, startOffset: Int, endOffset: Int): Array<PsiElement> {
        val firstMethod = elementAt(file, startOffset) ?: return EMPTY_ARRAY
        val lastMethod = elementAt(file, endOffset - 1) ?: return EMPTY_ARRAY
        val possibleMethods = firstMethod.parent
            .children
            .filterIsInstance<PsiMethod>()
            .between(firstMethod, lastMethod)
        return if (possibleMethods.none { it.modifierList.hasTestAnnotation() }) {
            EMPTY_ARRAY
        } else {
            PsiUtilCore.toPsiElementArray(possibleMethods)
        }
    }

    private fun elementAt(file: PsiFile, offset: Int): PsiMethod? {
        val result = file.viewProvider.findElementAt(offset, JavaLanguage.INSTANCE)
        return PsiTreeUtil.getParentOfType(result, PsiMethod::class.java, false)
    }
}