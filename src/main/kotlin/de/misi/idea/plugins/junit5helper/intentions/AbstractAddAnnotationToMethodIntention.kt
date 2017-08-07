package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifierList
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiTreeUtil.getChildrenOfType

abstract class AbstractAddAnnotationToMethodIntention(private val name: String) : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getFamilyName() = name

    override fun getText() = familyName

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val method = element.getParentOfType(PsiMethod::class.java)
        if (method != null) {
            val modifierList = getChildrenOfType(method, PsiModifierList::class.java)
            if (modifierList != null) {
                return !hasModifier(modifierList, getUnavailableAnnotation().replace("@", "")) &&
                        modifierList.any { it.hasTestAnnotation() }
            }
        }
        return false
    }

    private fun hasModifier(modifierList: Array<PsiModifierList>, name: String) =
            modifierList.any { it.hasAnnotationModifier(name) }

    protected abstract fun getUnavailableAnnotation(): String

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val method = element.getParentOfType(PsiMethod::class.java)
        if (method != null) {
            val factory = JavaPsiFacade.getInstance(project).elementFactory
            val annotation = factory.createAnnotationFromText(getCreateableAnnotation(), null)
            method.modifierList.add(annotation)
            CodeStyleManager.getInstance(project).reformat(method)
        }
    }

    open fun getCreateableAnnotation() = getUnavailableAnnotation()
}