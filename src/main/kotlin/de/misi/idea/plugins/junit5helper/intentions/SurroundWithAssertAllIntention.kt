package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import de.misi.idea.plugins.junit5helper.surround.findStatements
import de.misi.idea.plugins.junit5helper.surround.surroundWithAssertAll

class SurroundWithAssertAllIntention : PsiElementBaseIntentionAction(), IntentionAction {
    override fun getText() = familyName

    override fun getFamilyName() = SURROUND_WITH_ASSERT_ALL_NAME

    override fun startInWriteAction() = true

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement) =
            findStatements(editor?.caretModel?.currentCaret, element).isNotEmpty()

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        surroundWithAssertAll(project, editor, element)
    }
}