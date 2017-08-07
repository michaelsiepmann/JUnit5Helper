package de.misi.idea.plugins.junit5helper.intentions

class AddDisplayNameIntention : AbstractAddAnnotationIntention(ADD_DISPLAYNAME) {

    override fun getUnavailableAnnotation() = "@DisplayName"

    override fun getCreateableAnnotation() = "@DisplayName(\"\")"
}