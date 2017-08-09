package de.misi.idea.plugins.junit5helper.intentions

class RemoveDisabledFromClassIntention : AbstractRemoveAnnotationIntention("Disabled", REMOVE_DISABLED_FROM_CLASS, ::modifierListFromParentClass)