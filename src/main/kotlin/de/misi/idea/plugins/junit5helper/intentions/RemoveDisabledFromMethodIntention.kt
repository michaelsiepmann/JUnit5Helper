package de.misi.idea.plugins.junit5helper.intentions

import org.junit.jupiter.api.Disabled

class RemoveDisabledFromMethodIntention : AbstractRemoveAnnotationIntention(Disabled::class.java, REMOVE_DISABLED_FROM_METHOD, ::modifierListFromParentMethod)