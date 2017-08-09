package de.misi.idea.plugins.junit5helper.intentions

class RemoveDisabledFromMethodIntention : AbstractRemoveAnnotationIntention("Disabled", REMOVE_DISABLED_FROM_METHOD, ::modifierListFromParentMethod)