package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifierList
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.util.parentOfType

const val ADD_DISABLED_TO_CLASS = "Add @Disabled to class"
const val ADD_DISABLED_TO_METHOD = "Add @Disabled to method"
const val ADD_DISPLAYNAME = "Add @DisplayName"
const val REMOVE_DISABLED_FROM_CLASS = "Remove @Disabled from class"
const val REMOVE_DISABLED_FROM_METHOD = "Remove @Disabled from method"
const val REMOVE_DISPLAY_NAME_FROM_METHOD = "\"Remove DisplayName from method\""
const val CHANGE_TO_PARAMETERIZED_TEST = "Change to @ParameterizedTest"
const val CHANGE_TO_METHOD_SOURCE = "Change to @MethodSource"

const val ANNOTATION_DISABLED = "org.junit.jupiter.api.Disabled"
const val ANNOTATION_DISPLAY_NAME = "org.junit.jupiter.api.DisplayName"
const val ANNOTATION_CSV_SOURCE = "org.junit.jupiter.params.provider.CsvSource"
const val ANNOTATION_METHOD_SOURCE = "org.junit.jupiter.params.provider.MethodSource"
const val ANNOTATION_PARAMETERIZED_TEST = "org.junit.jupiter.params.ParameterizedTest"
const val ANNOTATION_TEST = "org.junit.jupiter.api.Test"

internal fun PsiElement.modifierListFromParentClass() =
    parentOfType<PsiClass>()?.modifierList

internal fun PsiElement.modifierListFromParentMethod() =
    parentOfType<PsiMethod>()?.modifierList

internal fun String.getSimpleClassName() = substring(lastIndexOf('.') + 1)

internal fun String.prependAtSign() = prependIfMissing("@")

private fun String.prependIfMissing(prefix: String) =
    if (startsWith(prefix)) {
        this
    } else {
        prefix + this
    }

internal fun PsiClass.addAnnotation(annotation: String, factory: PsiElementFactory, context: PsiElement) {
    val psiAnnotation = factory.createAnnotationFromText(annotation.prependAtSign(), context)
    modifierList?.add(psiAnnotation)
    JavaCodeStyleManager.getInstance(project).shortenClassReferences(context)
}

internal fun PsiModifierList.addAnnotation(annotation: String, factory: PsiElementFactory, context: PsiElement) {
    val psiAnnotation = factory.createAnnotationFromText(annotation.prependAtSign(), context)
    add(psiAnnotation)
    JavaCodeStyleManager.getInstance(project).shortenClassReferences(context)
}

internal fun PsiModifierList.deleteAnnotation(name: String) {
    findAnnotation(name)?.delete()
}

internal fun PsiModifierList.hasTestAnnotation() =
    hasAnnotationModifier(ANNOTATION_TEST) ||
            hasAnnotationModifier(ANNOTATION_PARAMETERIZED_TEST) ||
            hasAnnotationModifier(ANNOTATION_TEST.getSimpleClassName()) ||
            hasAnnotationModifier(ANNOTATION_PARAMETERIZED_TEST.getSimpleClassName())

internal fun PsiModifierList.hasAnnotationModifier(name: String) = findAnnotation(name) != null
