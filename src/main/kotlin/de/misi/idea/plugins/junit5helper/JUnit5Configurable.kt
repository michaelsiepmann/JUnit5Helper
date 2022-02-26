package de.misi.idea.plugins.junit5helper

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class JUnit5Configurable(private val project: Project) : Configurable {

    private lateinit var addDisplayName: JBCheckBox

    override fun createComponent(): JComponent {
        val bundle = JUnit5HelperBundle.INSTANCE
        val configuration = JUnit5WorkspaceConfiguration.getInstance(project)
        return panel {
            row {
                addDisplayName = checkBox(bundle.getMessage("configuration.addDisplayName"))
                    .component
                addDisplayName.isSelected = configuration.addDisplayName ?: false
            }
        }
    }

    override fun isModified(): Boolean {
        val configuration = JUnit5WorkspaceConfiguration.getInstance(project)
        return configuration.addDisplayName != addDisplayName.isSelected
    }

    override fun apply() {
        val configuration = JUnit5WorkspaceConfiguration.getInstance(project)
        configuration.addDisplayName = addDisplayName.isSelected
    }

    override fun getDisplayName() = "JUnit5 Helper"
}