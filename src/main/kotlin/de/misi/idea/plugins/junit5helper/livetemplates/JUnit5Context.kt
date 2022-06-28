package de.misi.idea.plugins.junit5helper.livetemplates

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiUtilCore.getLanguageAtOffset
import com.intellij.psi.util.parentOfType
import de.misi.idea.plugins.junit5helper.isTestFile

class JUnit5Context : TemplateContextType("JUNIT5", "JUnit 5") {

    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        val file = templateActionContext.file
        val offset = templateActionContext.startOffset
        return when {
            getLanguageAtOffset(file, offset).isKindOf(JavaLanguage.INSTANCE) ->
                file.findElementAt(offset)?.parentOfType<PsiClass>()?.isTestFile() ?: false
            else ->
                false
        }
    }
}
