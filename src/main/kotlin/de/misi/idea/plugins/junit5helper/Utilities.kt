package de.misi.idea.plugins.junit5helper

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.ModuleRootModel
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import org.jetbrains.jps.model.java.JavaSourceRootType
import java.text.Normalizer
import java.util.regex.Pattern

internal fun String.removeAccents(): String =
    Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(Normalizer.normalize(this, Normalizer.Form.NFD))
        .replaceAll("")

internal fun String.upperFirstChar(): String = convertFirstChar(this, String::uppercase)

internal fun String.lowerFirstChar(): String = convertFirstChar(this, String::lowercase)

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
internal fun PsiElement.isTestFile() = project.isInsideTestModule()

internal fun Project.isInsideTestModule() = ModuleManager.getInstance(this).isInsideTestModule()

internal fun ModuleManager.isInsideTestModule() = modules.any {
    it.isInsideTestModule()
}

internal fun Module.isInsideTestModule() = ModuleRootManager.getInstance(this).isInsideTestModule()

internal fun ModuleRootModel.isInsideTestModule(): Boolean {
    val contentEntry = contentEntries.find {
        it.sourceFolders.any {
            it.isTestSource
        }
    }
    return contentEntry?.sourceFolders?.first()?.rootType == JavaSourceRootType.TEST_SOURCE
}

internal fun <E> Collection<E>.between(start: E, end: E): Collection<E> {
    var inside = false
    return this.mapNotNull {
        if (inside) {
            if (it == end) {
                inside = false
            }
            it
        } else {
            if (it == start) {
                inside = true
                if (it == end) {
                    inside = false
                }
                it
            } else {
                null
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T : PsiElement> Project.shortenAndReformat(statement: T): T {
    val result = JavaCodeStyleManager.getInstance(this).shortenClassReferences(statement) as T
    return CodeStyleManager.getInstance(this).reformat(result) as T
}
