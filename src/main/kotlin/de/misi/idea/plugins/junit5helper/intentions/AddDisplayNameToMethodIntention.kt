package de.misi.idea.plugins.junit5helper.intentions

class AddDisplayNameToMethodIntention : AbstractAddAnnotationIntention("DisplayName", ADD_DISPLAYNAME, ::modifierListFromParentMethod)