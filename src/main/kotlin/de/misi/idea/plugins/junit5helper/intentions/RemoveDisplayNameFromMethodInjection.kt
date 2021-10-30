package de.misi.idea.plugins.junit5helper.intentions

class RemoveDisplayNameFromMethodInjection : AbstractRemoveAnnotationIntention(ANNOTATION_DISPLAY_NAME, "Remove DisplayName from method", ::modifierListFromParentMethod) {
}