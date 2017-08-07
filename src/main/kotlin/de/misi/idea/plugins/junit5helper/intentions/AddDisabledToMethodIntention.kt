package de.misi.idea.plugins.junit5helper.intentions

class AddDisabledToMethodIntention : AbstractAddAnnotationToMethodIntention(ADD_DISABLED_TO_METHOD) {

    override fun getUnavailableAnnotation() = "@Disabled"

    override fun getCreateableAnnotation() = "@Disabled(\"\")"
}