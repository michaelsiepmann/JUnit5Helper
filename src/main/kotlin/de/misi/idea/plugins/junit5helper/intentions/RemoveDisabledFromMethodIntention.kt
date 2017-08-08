package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod

class RemoveDisabledFromMethodIntention : AbstractRemoveAnnotationIntention("Disabled", REMOVE_DISABLED_FROM_METHOD) {

    override fun modifierList(element: PsiElement) =
            element.getParentOfType(PsiMethod::class.java)
                    ?.modifierList
}