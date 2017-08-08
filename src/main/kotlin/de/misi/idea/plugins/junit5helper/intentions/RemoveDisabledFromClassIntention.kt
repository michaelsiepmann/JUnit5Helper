package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement

class RemoveDisabledFromClassIntention : AbstractRemoveAnnotationIntention("Disabled", REMOVE_DISABLED_FROM_CLASS) {

    override fun modifierList(element: PsiElement) =
            element.getParentOfType(PsiClass::class.java)
                    ?.modifierList
}