package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

abstract class AbstractRemoveFromClassAnnotationIntention(
    private val annotation: String,
    private val name: String
) : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getFamilyName() = name

    override fun getText() = familyName

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement) =
        element.modifierListFromParentClass()?.hasAnnotationModifier(annotation.getSimpleClassName()) ?: false

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        element.modifierListFromParentClass()?.deleteAnnotation(annotation.getSimpleClassName())
    }
}