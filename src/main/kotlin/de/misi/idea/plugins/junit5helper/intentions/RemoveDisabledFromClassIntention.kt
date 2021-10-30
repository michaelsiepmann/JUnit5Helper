package de.misi.idea.plugins.junit5helper.intentions

class RemoveDisabledFromClassIntention : AbstractRemoveFromClassAnnotationIntention(
    ANNOTATION_DISABLED,
    REMOVE_DISABLED_FROM_CLASS
)