package de.misi.idea.plugins.junit5helper

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.ModuleRootModel
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.jetbrains.jps.model.java.JavaSourceRootType
import java.text.Normalizer
import java.util.regex.Pattern

internal fun String.removeAccents(): String = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(Normalizer.normalize(this, Normalizer.Form.NFD)).replaceAll("")

internal fun String.upperFirstChar(): String = convertFirstChar(this, String::toUpperCase)

internal fun String.lowerFirstChar(): String = convertFirstChar(this, String::toLowerCase)

private fun convertFirstChar(text: String, converter: (String.() -> String)): String {
    if (text.isEmpty()) {
        return text
    }
    if (text.length == 1) {
        return text.converter()
    }
    return text.substring(0, 1).converter() + text.substring(1)
}

// todo: Testen, ob man im Test-Directory eines Moduls ist.
internal fun PsiElement.isTestFile() = project.isInsideTestModule(containingFile)

internal fun Project.isInsideTestModule(file: PsiFile) = ModuleManager.getInstance(this).isInsideTestModule(file)

internal fun ModuleManager.isInsideTestModule(file: PsiFile) = modules.any {
    it.isInsideTestModule(file)
}

internal fun Module.isInsideTestModule(file: PsiFile) = ModuleRootManager.getInstance(this).isInsideTestModule(file)

internal fun ModuleRootModel.isInsideTestModule(file: PsiFile): Boolean {
    val contentEntry = contentEntries.find {
        it.sourceFolders.any {
            it.isTestSource
        }
    }
    return contentEntry?.sourceFolders?.first()?.rootType == JavaSourceRootType.TEST_SOURCE
}