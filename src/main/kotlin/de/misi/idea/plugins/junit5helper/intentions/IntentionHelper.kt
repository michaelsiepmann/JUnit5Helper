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
const val CHANGE_TO_PARAMETERIZED_TEST = "Change to @ParameterizedTest"
const val CHANGE_TO_METHOD_SOURCE = "Change to @MethodSource"

const val ANNOTATION_DISABLED = "org.junit.jupiter.api.Disabled"
const val ANNOTATION_DISPLAY_NAME = "org.junit.jupiter.api.DisplayName"
const val ANNOTATION_CSV_SOURCE = "org.junit.jupiter.params.provider.CsvSource"
const val ANNOTATION_METHOD_SOURCE = "org.junit.jupiter.params.provider.MethodSource"

const val SURROUND_WITH_NESTED_CLASS_NAME = "Surround with Nested-Class"

internal fun modifierListFromParentClass(element: PsiElement) =
    element.getParentOfType(PsiClass::class.java)
        ?.modifierList

internal fun modifierListFromParentMethod(element: PsiElement) =
    element.getParentOfType(PsiMethod::class.java)
        ?.modifierList

internal fun String.getSimpleClassName() = substring(lastIndexOf('.') + 1)

internal fun String.prependIfMissing(prefix: String) =
    if (startsWith(prefix)) {
        this
    } else {
        prefix + this
    }

internal fun PsiClass.addAnnotation(annotation: String, factory: PsiElementFactory, context: PsiElement) {
    val psiAnnotation = factory.createAnnotationFromText(annotation.prependIfMissing("@"), context)
    modifierList?.add(psiAnnotation)
    JavaCodeStyleManager.getInstance(project).shortenClassReferences(context)
}

internal fun PsiModifierList.addAnnotation(annotation: String, factory: PsiElementFactory, context: PsiElement) {
    val psiAnnotation = factory.createAnnotationFromText(annotation.prependIfMissing("@"), context)
    add(psiAnnotation)
    JavaCodeStyleManager.getInstance(project).shortenClassReferences(context)
}

internal fun <T : PsiElement> PsiElement.getParentOfType(aClass: Class<T>) = PsiTreeUtil.getParentOfType(this, aClass)

internal fun <T : PsiElement> PsiElement.hasParentOfType(aClass: Class<T>) = getParentOfType(aClass) != null

internal fun PsiModifierList.deleteAnnotation(name: String) {
    findAnnotation(name)?.delete()
}

internal fun PsiModifierList.hasTestAnnotation() =
    hasAnnotationModifier("Test") || hasAnnotationModifier("ParameterizedTest")

internal fun PsiModifierList.hasAnnotationModifier(name: String) = findAnnotation(name) != null
