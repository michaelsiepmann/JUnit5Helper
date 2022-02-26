package de.misi.idea.plugins.junit5helper

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class JUnit5Configurable(private val project: Project) : Configurable {

    private var addDisplayName = false

    override fun createComponent(): JComponent {
        val bundle = JUnit5HelperBundle.INSTANCE
        val configuration = JUnit5WorkspaceConfiguration.getInstance(project)
        addDisplayName = configuration.addDisplayName ?: false
        return panel {
            row {
                checkBox(bundle.getMessage("configuration.addDisplayName"))
                    .bindSelected(::addDisplayName)
            }
        }
    }

    override fun isModified(): Boolean {
        val configuration = JUnit5WorkspaceConfiguration.getInstance(project)
        return configuration.addDisplayName != addDisplayName
    }

    override fun apply() {
        val configuration = JUnit5WorkspaceConfiguration.getInstance(project)
        configuration.addDisplayName = addDisplayName
    }

    override fun getDisplayName() = "JUnit5 Helper"
}