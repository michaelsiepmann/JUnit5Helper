package de.misi.idea.plugins.junit5helper.surrounder

import com.intellij.lang.surroundWith.Surrounder
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiTreeUtil
import de.misi.idea.plugins.junit5helper.intentions.addAnnotation
import de.misi.idea.plugins.junit5helper.intentions.getParentOfType
import de.misi.idea.plugins.junit5helper.upperFirstChar

class NestedClassSurrounder : Surrounder {

    override fun getTemplateDescription() = "Nested class"

    override fun isApplicable(elements: Array<out PsiElement>) =
        elements.all { PsiTreeUtil.getParentOfType(it, PsiMethod::class.java, false) != null }

    override fun surroundElements(project: Project, editor: Editor, elements: Array<out PsiElement>): TextRange? {
        val method = PsiTreeUtil.getParentOfType(elements[0], PsiMethod::class.java, false) ?: return null
        val parentClazz = method.getParentOfType(PsiClass::class.java)
        if (parentClazz != null) {
            val factory = JavaPsiFacade.getInstance(project).elementFactory
            val clazz = factory.createClass(method.name.upperFirstChar() + "Test")
            clazz.addAnnotation("org.junit.jupiter.api.Nested", factory, clazz)
            clazz.addAnnotation("org.junit.jupiter.api.DisplayName(\"\")", factory, clazz)
            clazz.modifierList?.setModifierProperty("public", false)
            val index = parentClazz.children.indexOf(method)
            elements.forEach {
                clazz.add(it)
                parentClazz.children[parentClazz.children.indexOf(it)].delete()
            }
            parentClazz.addBefore(clazz, parentClazz.children[index])
            CodeStyleManager.getInstance(project).reformat(clazz)
            return TextRange(method.textRange.startOffset, method.textRange.startOffset)
        }
        return null
    }
}