package de.misi.idea.plugins.junit5helper

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "junit5WorkspaceConfiguration", storages = [Storage("junit5WorkspaceConfiguration.xml")])
class JUnit5WorkspaceConfiguration : PersistentStateComponent<JUnit5WorkspaceConfiguration> {

    var addDisplayName: Boolean? = null
        get() {
            if (field == null) {
                field = false
            }
            return field
        }

    override fun getState() = this

    override fun loadState(state: JUnit5WorkspaceConfiguration) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(project: Project): JUnit5WorkspaceConfiguration =
            project.getService(JUnit5WorkspaceConfiguration::class.java)
    }
}