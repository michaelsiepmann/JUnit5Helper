package de.misi.idea.plugins.junit5helper;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class JUnit5ConfigurationDialog {
    private JPanel rootPanel;
    private JCheckBox addDisplayName;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JUnit5Configuration getConfiguration() {
        return new JUnit5Configuration(
                addDisplayName.isSelected()
        );
    }

    public void setConfiguration(JUnit5Configuration configuration) {
        addDisplayName.setSelected(configuration.getAddDisplayName());
    }
}
