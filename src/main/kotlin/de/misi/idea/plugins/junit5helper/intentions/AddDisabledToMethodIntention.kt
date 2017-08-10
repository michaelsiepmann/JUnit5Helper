package de.misi.idea.plugins.junit5helper.intentions

class AddDisabledToMethodIntention : AbstractAddAnnotationIntention("org.junit.jupiter.api.Disabled", ADD_DISABLED_TO_METHOD, ::modifierListFromParentMethod)