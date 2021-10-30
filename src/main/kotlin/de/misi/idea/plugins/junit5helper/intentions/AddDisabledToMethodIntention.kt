package de.misi.idea.plugins.junit5helper.intentions

class AddDisabledToMethodIntention : AbstractAddAnnotationIntention(ANNOTATION_DISABLED, ADD_DISABLED_TO_METHOD, ::modifierListFromParentMethod)