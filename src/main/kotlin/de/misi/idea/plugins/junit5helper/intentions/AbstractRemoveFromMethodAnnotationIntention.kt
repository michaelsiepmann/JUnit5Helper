package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

abstract class AbstractRemoveFromMethodAnnotationIntention(private val annotation: String, name: String) :
    AbstractTestMethodAnnotation(name) {

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement) =
        element.modifierListFromParentMethod()?.hasAnnotationModifier(annotation.getSimpleClassName()) ?: false &&
                super.isAvailable(project, editor, element)

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        element.modifierListFromParentMethod()?.deleteAnnotation(annotation.getSimpleClassName())
    }
}