package de.misi.idea.plugins.junit5helper.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.parentOfType
import com.opencsv.CSVParser
import de.misi.idea.plugins.junit5helper.upperFirstChar

class ConvertCSVSourceToMethodSourceIntention : PsiElementBaseIntentionAction(), IntentionAction {
    override fun getText() = familyName

    override fun getFamilyName() = CHANGE_TO_METHOD_SOURCE

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return element.modifierListFromParentMethod()?.hasAnnotationModifier(ANNOTATION_CSV_SOURCE)
            ?: false
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        element.modifierListFromParentMethod()?.let { modifierList ->
            val csvParser = CSVParser()
            val lines = modifierList.findAnnotation(ANNOTATION_CSV_SOURCE)
                ?.findAttributeValue("value")
                ?.children
                ?.filterIsInstance<PsiLiteralExpression>()
                ?.joinToString(separator = ",\n") { expression ->
                    csvParser
                        .parseLine(expression.text.replace(Regex("^\"(.*)\"$"), "$1"))
                        .joinToString(
                            separator = ", ",
                            prefix = "Arguments.of(",
                            postfix = ")"

                        ) {
                            it.convertValue()
                        }
                }
                ?: return
            val factory = PsiElementFactory.getInstance(project)
            val method = modifierList.parentOfType<PsiMethod>() ?: return
            val clazz = element.parentOfType<PsiClass>() ?: return
            val methodName = "methodSourceFor" + method.name.upperFirstChar()
            val methodeSourceMethod = factory.createMethodFromText(
                """private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> $methodName() {
                |   return Stream.of(
                |      $lines
                |   );
                |}
            """.trimMargin(), clazz
            )
            clazz.addAfter(methodeSourceMethod, method)
            modifierList.addAnnotation(
                "$ANNOTATION_METHOD_SOURCE(\"$methodName\")",
                factory,
                clazz
            )
            modifierList.deleteAnnotation(ANNOTATION_CSV_SOURCE)
        }
    }

    private fun String.convertValue() =
        when (this) {
            "" -> "null"
            else -> replace(Regex("^'(.*)'$"), "\"$1\"")
        }
}