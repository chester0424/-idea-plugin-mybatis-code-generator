package zone.pusu.mybatisCodeGenerator.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zone.pusu.mybatisCodeGenerator.setting.SettingMainStoreService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SettingMain implements Configurable, Configurable.Composite {

    private boolean modifiedFlag = false;
    private String authorName = "";

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "MyBatis CodeGenerator FWD DIR";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return getMainUI();
    }

    @Override
    public boolean isModified() {
        return !SettingMainStoreService.getInstance().getState().getAuthorName().equals(authorName);
    }

    @Override
    public void apply() throws ConfigurationException {
        SettingMainStoreService.getInstance().getState().setAuthorName(authorName);
    }

    @Override
    public @NotNull Configurable[] getConfigurables() {
        Configurable[] configurables = new Configurable[]{
                new SettingTemplate()
        };
        return configurables;
    }

    private JPanel getMainUI() {
        // 初始数据
        authorName = SettingMainStoreService.getInstance().getState().getAuthorName();

        // 构造UI视图
        JPanel jPanelContainer = new JPanel();
        jPanelContainer.setLayout(new VerticalFlowLayout(VerticalFlowLayout.LEFT));

        JPanel jPanelAuthor = new JPanel();
        jPanelAuthor.setLayout(new FlowLayout(FlowLayout.LEFT));
        // 用户信息
        jPanelAuthor.add(new JLabel("Author Name:"));
        JTextField textFieldAuthorName = new JTextField();
        textFieldAuthorName.setPreferredSize(new Dimension(400, 30));
        textFieldAuthorName.setText(authorName);
        textFieldAuthorName.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                authorName = textFieldAuthorName.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                authorName = textFieldAuthorName.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                authorName = textFieldAuthorName.getText();
            }
        });
        jPanelAuthor.add(textFieldAuthorName);
        jPanelContainer.add(jPanelAuthor);

        return jPanelContainer;
    }
}
