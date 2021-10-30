package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import de.misi.idea.plugins.junit5helper.surround.shortenAndReformat

abstract class AbstractAddToClassAnnotationIntention(
    private val annotationClazz: String,
    private val name: String
) : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getFamilyName() = name

    override fun getText() = familyName

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val modifierList = element.modifierListFromParentClass() ?: return false
        return !modifierList.hasAnnotationModifier(annotationClazz.getSimpleClassName())
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val method = element.getParentOfType(PsiMethod::class.java) ?: return
        val psiFacade = JavaPsiFacade.getInstance(project)
        val factory = psiFacade.elementFactory
        val annotation = factory.createAnnotationFromText("@${annotationClazz}(\"\")", null)
        element.modifierListFromParentClass()?.add(annotation)
        project.shortenAndReformat(method)
    }
}