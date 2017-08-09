package de.misi.idea.plugins.junit5helper.intentions

import org.junit.jupiter.api.Disabled

class AddDisabledToMethodIntention : AbstractAddAnnotationIntention(Disabled::class.java.canonicalName, ADD_DISABLED_TO_METHOD, ::modifierListFromParentMethod)