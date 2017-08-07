package de.misi.idea.plugins.junit5helper.livetemplates

import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import com.intellij.psi.util.PsiUtilCore.getLanguageAtOffset

class JUnit5Context : TemplateContextType("JUNIT5", "JUnit 5") {

    override fun isInContext(file: PsiFile, offset: Int): Boolean {
        if (getLanguageAtOffset(file, offset).isKindOf(JavaLanguage.INSTANCE)) {
            val clazz = getParentOfType(file.findElementAt(offset), PsiClass::class.java)
            if (clazz != null) {
                // todo: Testen, ob man im Test-Directory eines Moduls ist.
                return true
            }
        }
        return false
    }
}