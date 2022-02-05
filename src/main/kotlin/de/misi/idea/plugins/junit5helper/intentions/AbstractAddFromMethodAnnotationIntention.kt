package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import de.misi.idea.plugins.junit5helper.shortenAndReformat

abstract class AbstractAddFromMethodAnnotationIntention(private val annotationClazz: String, name: String) :
    AbstractTestMethodAnnotation(name) {

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val modifierList = element.modifierListFromParentMethod() ?: return false
        return !modifierList.hasAnnotationModifier(annotationClazz.getSimpleClassName()) &&
                super.isAvailable(project, editor, element)
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val method = element.getParentOfType(PsiMethod::class.java) ?: return
        val psiFacade = JavaPsiFacade.getInstance(project)
        val factory = psiFacade.elementFactory
        val annotation = factory.createAnnotationFromText("@${annotationClazz}(\"\")", null)
        element.modifierListFromParentMethod()?.add(annotation)
        project.shortenAndReformat(method)
    }
}