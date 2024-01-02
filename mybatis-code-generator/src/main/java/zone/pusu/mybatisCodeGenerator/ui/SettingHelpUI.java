package zone.pusu.mybatisCodeGenerator.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class SettingHelpUI implements Configurable {
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Use Guide";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return initUI();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    private JComponent initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JEditorPane jEditorPane = new JEditorPane();
        jEditorPane.setEditable(false);
        URL url = this.getClass().getResource("/help.html");
        jEditorPane.setContentType("text/html");
        jEditorPane.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(e.getURL().toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        try {
            jEditorPane.setPage(url);
        } catch (IOException e) {
            jEditorPane.setText("<html>Page not found.</html>");
        }

        JScrollPane jScrollPane = new JBScrollPane(jEditorPane);
        jScrollPane.setPreferredSize(new Dimension(540, 400));

        panel.add(jScrollPane);

        return panel;
    }
}
