package de.misi.idea.junit5.extensions

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext

internal fun <A : Annotation> ExtensionContext.getAnnotation(clazz: Class<A>) =
        when {
            requiredTestMethod.isAnnotationPresent(clazz) -> requiredTestMethod.getAnnotation(clazz)
            requiredTestClass.isAnnotationPresent(clazz) -> requiredTestClass.getAnnotation(clazz)
            else -> null
        }

internal fun ParameterContext.isOfType(clazz : Class<*>) = parameter?.type?.name == clazz.name ?: false