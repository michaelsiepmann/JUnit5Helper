package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod

class RemoveDisabledFromMethodIntention : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getFamilyName() = REMOVE_DISABLED_FROM_METHOD

    override fun getText() = familyName

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val method = element.getParentOfType(PsiMethod::class.java)
        return method?.modifierList?.hasAnnotationModifier("Disabled") ?: false
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val method = element.getParentOfType(PsiMethod::class.java)
        if (method?.modifierList?.hasAnnotationModifier("Disabled") ?: false) {
            method?.modifierList?.deleteAnnotation("Disabled")
        }
    }
}