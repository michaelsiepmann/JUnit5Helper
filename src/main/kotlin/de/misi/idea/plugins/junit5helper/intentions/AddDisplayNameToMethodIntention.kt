package de.misi.idea.plugins.junit5helper.intentions

class AddDisplayNameToMethodIntention : AbstractAddAnnotationIntention(ANNOTATION_DISPLAY_NAME, ADD_DISPLAYNAME, ::modifierListFromParentMethod)