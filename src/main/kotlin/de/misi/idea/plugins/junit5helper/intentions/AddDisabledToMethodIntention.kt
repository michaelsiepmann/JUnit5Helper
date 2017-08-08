package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifierList

class AddDisabledToMethodIntention : AbstractAddAnnotationIntention("Disabled", ADD_DISABLED_TO_METHOD) {

    override fun modifierList(element: PsiElement) =
            element.getParentOfType(PsiMethod::class.java)?.getChildOfType(PsiModifierList::class.java)
}