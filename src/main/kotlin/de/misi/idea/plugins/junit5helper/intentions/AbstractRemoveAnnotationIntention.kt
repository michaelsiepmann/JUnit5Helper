package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifierList

abstract class AbstractRemoveAnnotationIntention(
        private val annotation: String,
        private val name: String,
        private val modifierList: ((PsiElement) -> PsiModifierList?)
) : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getFamilyName() = name

    override fun getText() = familyName

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement) =
            modifierList(element)?.hasAnnotationModifier(annotation) ?: false

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        modifierList(element)?.deleteAnnotation(annotation)
    }
}