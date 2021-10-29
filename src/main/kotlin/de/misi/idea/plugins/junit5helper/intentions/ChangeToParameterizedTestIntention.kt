package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.util.parentOfType

class ChangeToParameterizedTestIntention : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText() = familyName

    override fun getFamilyName() = CHANGE_TO_PARAMETERIZED_TEST

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement) =
        modifierListFromParentMethod(element)?.hasAnnotationModifier("org.junit.jupiter.api.Test") ?: false

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        modifierListFromParentMethod(element)?.let {
            val factory = PsiElementFactory.getInstance(project)
            val clazz = element.parentOfType<PsiClass>() ?: return
            it.addAnnotation("@org.junit.jupiter.params.ParameterizedTest(name=\"\")", factory, clazz)
            it.addAnnotation("@org.junit.jupiter.params.provider.CsvSource({\n\"\"\n})", factory, clazz)
            it.deleteAnnotation("org.junit.jupiter.api.Test")
        }
    }
}