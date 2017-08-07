package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.util.PsiTreeUtil

const val ADD_DISABLED = "Add @Disabled"
const val ADD_DISPLAYNAME = "Add @DisplayName"
const val REMOVE_DISABLED_FROM_METHOD = "Remove @Disabled from method"
const val REMOVE_DISABLED_FROM_CLASS = "Remove @Disabled from class"
const val SURROUND_WITH_ASSERT_ALL_NAME = "Surround with AssertAll"
const val SURROUND_WITH_NESTED_CLASS_NAME = "Surround with Nested-Class"

fun PsiClass.addAnnotation(annotation: String, factory: PsiElementFactory) {
    modifierList?.add(factory.createAnnotationFromText(annotation, null))
}

fun PsiClass.addImportStatement(factory: PsiElementFactory, importClass: String, project: Project) {
    getParentOfType(PsiJavaFile::class.java)
            ?.addImportStatement(factory, importClass, project)
}

fun <T : PsiElement> PsiElement.getParentOfType(aClass: Class<T>) = PsiTreeUtil.getParentOfType(this, aClass)

fun <T : PsiElement> PsiElement.hasParentOfType(aClass: Class<T>) = getParentOfType(aClass) != null

fun PsiJavaFile.addImportStatement(factory: PsiElementFactory, importClass: String, project: Project) {
    importList?.addImportStatement(factory, importClass, project)
}

fun PsiImportList.addImportStatement(factory: PsiElementFactory, importClass: String, project: Project) {
    val importStatement = findSingleImportStatement(importClass)
    if (importStatement == null) {
        val newClazz = JavaPsiFacade.getInstance(project).findClass(importClass, ProjectScope.getLibrariesScope(project))
        if (newClazz != null) {
            add(factory.createImportStatement(newClazz))
            CodeStyleManager.getInstance(project).reformat(this)
        }
    }
}

fun PsiModifierList.deleteAnnotation(name: String) {
    findAnnotation(name)?.delete()
}

fun PsiModifierList.hasTestAnnotation() = hasAnnotationModifier("Test") || hasAnnotationModifier("ParameterizedTest")

fun PsiModifierList.hasAnnotationModifier(name: String) = findAnnotation(name) != null
