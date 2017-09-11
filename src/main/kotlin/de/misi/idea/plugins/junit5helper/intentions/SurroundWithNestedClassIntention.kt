package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import de.misi.idea.plugins.junit5helper.surround.surroundWithNestedClass

class SurroundWithNestedClassIntention : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getFamilyName() = SURROUND_WITH_NESTED_CLASS_NAME

    override fun getText() = familyName

    override fun startInWriteAction() = true

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement) =
            element.hasParentOfType(PsiMethod::class.java)

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        surroundWithNestedClass(element, project)
    }
}