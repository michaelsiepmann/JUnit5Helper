package de.misi.idea.plugins.junit5helper

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

class JUnit5Configurable(private val project: Project) : Configurable {

    private var panel: JUnit5ConfigurationDialog? = null

    override fun createComponent(): JComponent = getPanel().rootPanel

    override fun isModified(): Boolean {
        val configuration = JUnit5WorkspaceConfiguration.getInstance(project)
        val panelConfig = getPanel().configuration
        return configuration.addDisplayName != panelConfig.addDisplayName
    }

    override fun apply() {
        val configuration = JUnit5WorkspaceConfiguration.getInstance(project)
        val panelConfig = getPanel().configuration
        configuration.addDisplayName = panelConfig.addDisplayName
    }

    override fun getDisplayName() = "JUnit5 Helper"

    private fun getPanel(): JUnit5ConfigurationDialog {
        if (panel == null) {
            val configuration = JUnit5WorkspaceConfiguration.getInstance(project)
            panel = JUnit5ConfigurationDialog()
            panel?.configuration = JUnit5Configuration(
                configuration.addDisplayName ?: false
            )
        }
        return panel!!
    }
}