package de.misi.idea.plugins.junit5helper.intentions

import org.junit.jupiter.api.Disabled

class RemoveDisabledFromClassIntention : AbstractRemoveAnnotationIntention(Disabled::class.java, REMOVE_DISABLED_FROM_CLASS, ::modifierListFromParentClass)