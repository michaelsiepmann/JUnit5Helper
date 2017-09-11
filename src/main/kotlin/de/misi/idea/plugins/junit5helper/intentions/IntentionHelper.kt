package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifierList
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.util.PsiTreeUtil

const val ADD_DISABLED_TO_CLASS = "Add @Disabled to class"
const val ADD_DISABLED_TO_METHOD = "Add @Disabled to method"
const val ADD_DISPLAYNAME = "Add @DisplayName"
const val REMOVE_DISABLED_FROM_CLASS = "Remove @Disabled from class"
const val REMOVE_DISABLED_FROM_METHOD = "Remove @Disabled from method"
const val SURROUND_WITH_ASSERT_ALL_NAME = "Surround with AssertAll"

const val SURROUND_WITH_NESTED_CLASS_NAME = "Surround with Nested-Class"

internal fun modifierListFromParentClass(element: PsiElement) =
        element.getParentOfType(PsiClass::class.java)
                ?.modifierList

internal fun modifierListFromParentMethod(element: PsiElement) =
        element.getParentOfType(PsiMethod::class.java)
                ?.modifierList

internal fun String.getSimpleClassName() = substring(lastIndexOf('.') + 1)

internal fun PsiClass.addAnnotation(annotation: String, factory: PsiElementFactory, context: PsiElement) {
    val psiAnnotation = factory.createAnnotationFromText(annotation, context)
    modifierList?.add(psiAnnotation)
    JavaCodeStyleManager.getInstance(project).shortenClassReferences(context)
}

internal fun <T : PsiElement> PsiElement.getParentOfType(aClass: Class<T>) = PsiTreeUtil.getParentOfType(this, aClass)

internal fun <T : PsiElement> PsiElement.hasParentOfType(aClass: Class<T>) = getParentOfType(aClass) != null

internal fun PsiModifierList.deleteAnnotation(name: String) {
    findAnnotation(name)?.delete()
}

internal fun PsiModifierList.hasTestAnnotation() = hasAnnotationModifier("Test") || hasAnnotationModifier("ParameterizedTest")

internal fun PsiModifierList.hasAnnotationModifier(name: String) = findAnnotation(name) != null
