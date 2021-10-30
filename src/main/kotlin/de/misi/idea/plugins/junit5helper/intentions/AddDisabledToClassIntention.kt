package de.misi.idea.plugins.junit5helper.intentions

class AddDisabledToClassIntention : AbstractAddAnnotationIntention(ANNOTATION_DISABLED, ADD_DISABLED_TO_CLASS, ::modifierListFromParentClass)