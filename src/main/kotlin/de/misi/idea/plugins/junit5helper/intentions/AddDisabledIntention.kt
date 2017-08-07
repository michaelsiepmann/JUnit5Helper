package de.misi.idea.plugins.junit5helper.intentions

class AddDisabledIntention : AbstractAddAnnotationIntention(ADD_DISABLED) {

    override fun getUnavailableAnnotation() = "@Disabled"

    override fun getCreateableAnnotation() = "@Disabled(\"\")"
}