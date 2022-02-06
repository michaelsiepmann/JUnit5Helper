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
        element.modifierListFromParentMethod()?.hasAnnotationModifier(ANNOTATION_TEST) ?: false

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        element.modifierListFromParentMethod()?.let {
            val factory = PsiElementFactory.getInstance(project)
            val clazz = element.parentOfType<PsiClass>() ?: return
            it.addAnnotation("$ANNOTATION_PARAMETERIZED_TEST(name=\"\")", factory, clazz)
            it.addAnnotation("$ANNOTATION_CSV_SOURCE({\n\"\"\n})", factory, clazz)
            it.deleteAnnotation(ANNOTATION_TEST)
        }
    }
}