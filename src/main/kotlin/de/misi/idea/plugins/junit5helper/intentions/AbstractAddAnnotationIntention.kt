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

abstract class AbstractAddAnnotationIntention(
        private val annotationClazz: String,
        private val name: String,
        private val modifierList: ((PsiElement) -> PsiModifierList?)
) : PsiElementBaseIntentionAction(), IntentionAction {

    private val annotation = annotationClazz.substring(annotationClazz.lastIndexOf('.') + 1)

    override fun getFamilyName() = name

    override fun getText() = familyName

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val modifierList = modifierList(element)
        if (modifierList != null) {
            return !modifierList.hasAnnotationModifier(annotation) &&
                    modifierList.hasTestAnnotation()
        }
        return false
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val method = element.getParentOfType(PsiMethod::class.java)
        if (method != null) {
            val psiFacade = JavaPsiFacade.getInstance(project)
            val factory = psiFacade.elementFactory
            val annotation = factory.createAnnotationFromText("@$annotationClazz(\"\")", null)
            modifierList(element)?.add(annotation)
            CodeStyleManager.getInstance(project).reformat(method)
        }
    }
}