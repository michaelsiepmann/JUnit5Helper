package de.misi.idea.plugins.junit5helper.surrounder

import com.intellij.lang.surroundWith.Surrounder
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.parentOfType
import de.misi.idea.plugins.junit5helper.intentions.addAnnotation
import de.misi.idea.plugins.junit5helper.shortenAndReformat
import de.misi.idea.plugins.junit5helper.upperFirstChar

class NestedClassSurrounder : Surrounder {

    override fun getTemplateDescription() = "Nested class"

    override fun isApplicable(elements: Array<out PsiElement>) =
        elements.all { it.parentOfType<PsiMethod>(true) != null }

    override fun surroundElements(project: Project, editor: Editor, elements: Array<out PsiElement>): TextRange? {
        val firstElement = elements[0]
        val method = firstElement.parentOfType<PsiMethod>(true) ?: return null
        val parentClazz = method.parentOfType<PsiClass>()
        if (parentClazz != null) {
            val factory = JavaPsiFacade.getInstance(project).elementFactory
            val clazzName = method.name.upperFirstChar() + "Test"
            val clazz = factory.createClassFromText("", firstElement)
            clazz.setName(clazzName)
            clazz.addAnnotation("org.junit.jupiter.api.Nested", factory, clazz)
            clazz.addAnnotation("org.junit.jupiter.api.DisplayName(\"\")", factory, clazz)
            clazz.addRangeBefore(firstElement, elements[elements.size - 1], clazz.rBrace)
            val parentNode = firstElement.parent.node
            if (elements.size > 1) {
                parentNode.removeRange(firstElement.node.treeNext, elements[elements.size - 1].node.treeNext)
            }
            parentNode.replaceChild(firstElement.node, clazz.node)
            project.shortenAndReformat(clazz)
            val id = clazz.nameIdentifier
            return TextRange(
                id?.textRange?.startOffset ?: method.textRange.startOffset,
                id?.textRange?.endOffset ?: method.textRange.startOffset
            )
        }
        return null
    }
}