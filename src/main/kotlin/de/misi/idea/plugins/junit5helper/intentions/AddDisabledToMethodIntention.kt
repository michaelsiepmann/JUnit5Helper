package de.misi.idea.plugins.junit5helper.intentions

class AddDisabledToMethodIntention : AbstractAddAnnotationIntention("Disabled", ADD_DISABLED_TO_METHOD, ::modifierListFromParentMethod)