package de.misi.idea.plugins.junit5helper.intentions

class AddDisplayNameToMethodIntention : AbstractAddAnnotationIntention("org.junit.jupiter.api.DisplayName", ADD_DISPLAYNAME, ::modifierListFromParentMethod)