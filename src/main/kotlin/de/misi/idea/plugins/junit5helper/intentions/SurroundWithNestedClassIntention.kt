package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.codeStyle.CodeStyleManager
import de.misi.idea.plugins.junit5helper.upperFirstChar

class SurroundWithNestedClassIntention : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getFamilyName() = SURROUND_WITH_NESTED_CLASS_NAME

    override fun getText() = familyName

    override fun startInWriteAction() = true

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement) =
            element.hasParentOfType(PsiMethod::class.java)

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val method = element.getParentOfType(PsiMethod::class.java)
        if (method != null) {
            val parentClazz = method.getParentOfType(PsiClass::class.java)
            if (parentClazz != null) {
                val factory = JavaPsiFacade.getInstance(project).elementFactory
                val clazz = factory.createClass(method.name.upperFirstChar() + "Test")
                clazz.addAnnotation("@Nested", factory)
                clazz.addAnnotation("@DisplayName(\"\")", factory)
                parentClazz.addImportStatement(factory, "org.junit.jupiter.api.Nested", project)
                parentClazz.addImportStatement(factory, "org.junit.jupiter.api.DisplayName", project)
                clazz.modifierList?.setModifierProperty("public", false)
                clazz.add(method)
                val index = parentClazz.children.indexOf(method)
                parentClazz.children[index].delete()
                parentClazz.addBefore(clazz, parentClazz.children[index])
                CodeStyleManager.getInstance(project).reformat(clazz)
            }
        }
    }
}