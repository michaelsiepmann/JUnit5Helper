package de.misi.idea.plugins.junit5helper.intentions

import org.junit.jupiter.api.DisplayName

class AddDisplayNameToMethodIntention : AbstractAddAnnotationIntention(DisplayName::class.java, ADD_DISPLAYNAME, ::modifierListFromParentMethod)