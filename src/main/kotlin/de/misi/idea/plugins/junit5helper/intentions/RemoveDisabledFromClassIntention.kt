package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement

class RemoveDisabledFromClassIntention : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getFamilyName() = REMOVE_DISABLED_FROM_CLASS

    override fun getText() = familyName

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return element.getParentOfType(PsiClass::class.java)
                ?.modifierList
                ?.hasAnnotationModifier("Disabled") ?: false
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        element.getParentOfType(PsiClass::class.java)
                ?.modifierList
                ?.deleteAnnotation("Disabled")
    }
}